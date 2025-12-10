from aiokafka import AIOKafkaProducer, AIOKafkaConsumer
from aiokafka.errors import KafkaError, KafkaConnectionError
import json
import asyncio
from typing import Optional
from loguru import logger

from app.config import settings
from app.prophet_service import prediction_service


class KafkaService:
    """Service for Kafka integration with automatic retry"""

    def __init__(self):
        self.producer: Optional[AIOKafkaProducer] = None
        self.consumer: Optional[AIOKafkaConsumer] = None
        self.bootstrap_servers = settings.KAFKA_BOOTSTRAP_SERVERS
        self.consumer_running = False
        self.max_retries = 10
        self.retry_delay = 5  # seconds

    async def start_producer(self):
        """Start Kafka producer with retry logic"""
        retry_count = 0
        while retry_count < self.max_retries:
            try:
                self.producer = AIOKafkaProducer(
                    bootstrap_servers=self.bootstrap_servers,
                    value_serializer=lambda v: json.dumps(v).encode('utf-8'),
                    request_timeout_ms=30000,
                    retry_backoff_ms=1000
                )
                await self.producer.start()
                logger.info("Kafka producer started successfully")
                return
            except (KafkaError, KafkaConnectionError) as e:
                retry_count += 1
                logger.warning(
                    f"Failed to start Kafka producer (attempt {retry_count}/{self.max_retries}): {e}. "
                    f"Retrying in {self.retry_delay}s..."
                )
                if retry_count < self.max_retries:
                    await asyncio.sleep(self.retry_delay)
                else:
                    logger.error("Max retries reached. Kafka producer will not be available.")
                    raise
            except Exception as e:
                logger.error(f"Unexpected error starting Kafka producer: {e}")
                raise

    async def stop_producer(self):
        """Stop Kafka producer"""
        if self.producer:
            await self.producer.stop()
            logger.info("Kafka producer stopped")

    async def start_consumer(self):
        """Start Kafka consumer with retry logic and auto-create topics"""
        retry_count = 0
        while retry_count < self.max_retries:
            try:
                self.consumer = AIOKafkaConsumer(
                    settings.KAFKA_TOPIC_STOCK_UPDATES,
                    bootstrap_servers=self.bootstrap_servers,
                    group_id=settings.KAFKA_GROUP_ID,
                    value_deserializer=lambda m: json.loads(m.decode('utf-8')),
                    auto_offset_reset='latest',
                    enable_auto_commit=True,
                    # Retry and timeout settings
                    request_timeout_ms=30000,
                    retry_backoff_ms=1000,
                    max_poll_interval_ms=300000,
                    session_timeout_ms=60000,
                    heartbeat_interval_ms=20000,
                    # Connection settings
                    connections_max_idle_ms=540000,
                    metadata_max_age_ms=300000
                )
                await self.consumer.start()
                self.consumer_running = True
                logger.info(
                    f"✅ Kafka consumer started successfully for topic: {settings.KAFKA_TOPIC_STOCK_UPDATES}"
                )
                return
            except Exception as e:
                retry_count += 1
                logger.warning(
                    f"Failed to start Kafka consumer (attempt {retry_count}/{self.max_retries}): {e}. "
                    f"Topic may not exist yet, will be auto-created. Retrying in {self.retry_delay}s..."
                )
                if retry_count < self.max_retries:
                    await asyncio.sleep(self.retry_delay)
                else:
                    logger.error(
                        "Max retries reached for Kafka consumer. "
                        "Consumer will not be available until service restart."
                    )
                    self.consumer_running = False
                    # Don't raise - service can still function without consumer
                    return

    async def stop_consumer(self):
        """Stop Kafka consumer"""
        if self.consumer:
            await self.consumer.stop()
            logger.info("Kafka consumer stopped")

    async def publish_prediction(self, prediction: dict):
        """Publish prediction to Kafka with error handling"""
        try:
            if self.producer is None:
                logger.warning("Kafka producer not available, skipping publish")
                return False

            await self.producer.send_and_wait(
                settings.KAFKA_TOPIC_PREDICTIONS,
                prediction
            )
            logger.info(f"✅ Published prediction for {prediction.get('symbol')} to Kafka")
            return True
        except KafkaError as e:
            logger.warning(f"Kafka error publishing prediction: {e} (prediction saved to DB)")
            return False
        except Exception as e:
            logger.warning(f"Error publishing prediction: {e} (prediction saved to DB)")
            return False

    async def consume_stock_updates(self):
        """Consume stock update events with auto-reconnect"""
        if not self.consumer_running:
            logger.warning("Kafka consumer not available, skipping consumption")
            return
            
        try:
            if self.consumer is None:
                logger.warning("Consumer not initialized")
                return

            logger.info("🎧 Started listening for stock updates...")
            async for message in self.consumer:
                try:
                    data = message.value
                    symbol = data.get('symbol')
                    
                    if symbol:
                        logger.info(f"📨 Received stock update for {symbol}")
                        
                        # Trigger prediction (async, don't wait)
                        asyncio.create_task(
                            self._process_stock_update(symbol)
                        )
                        
                except Exception as e:
                    logger.error(f"Error processing message: {e}")
                    continue

        except Exception as e:
            logger.error(f"Consumer loop error: {e}. Will attempt reconnect on next service interaction.")
            self.consumer_running = False
    
    async def _process_stock_update(self, symbol: str):
        """Process stock update in background"""
        try:
            recommendation = await prediction_service.generate_recommendation(
                symbol=symbol
            )
            
            if recommendation:
                await self.publish_prediction(recommendation)
            
        except Exception as e:
            logger.error(f"Error processing stock update for {symbol}: {e}")


# Global instance
kafka_service = KafkaService()
