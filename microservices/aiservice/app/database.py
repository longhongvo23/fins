from motor.motor_asyncio import AsyncIOMotorClient
from pymongo import ASCENDING, DESCENDING
from typing import Optional, List
from datetime import datetime, timedelta
from loguru import logger

from app.config import settings


class MongoDBService:
    def __init__(self):
        self.client: Optional[AsyncIOMotorClient] = None
        self.db = None

    async def connect(self):
        """Connect to MongoDB"""
        try:
            self.client = AsyncIOMotorClient(settings.MONGODB_URI)
            self.db = self.client[settings.MONGODB_DATABASE]
            
            # Test connection
            await self.client.admin.command('ping')
            logger.info(f"Connected to MongoDB: {settings.MONGODB_DATABASE}")
            
            # Create indexes
            await self._create_indexes()
        except Exception as e:
            logger.error(f"Failed to connect to MongoDB: {e}")
            raise

    async def disconnect(self):
        """Disconnect from MongoDB"""
        if self.client:
            self.client.close()
            logger.info("Disconnected from MongoDB")

    async def _create_indexes(self):
        """Create necessary indexes"""
        try:
            # Historical Price indexes
            historical_collection = self.db[settings.HISTORICAL_PRICE_COLLECTION]
            await historical_collection.create_index([("symbol", ASCENDING)])
            await historical_collection.create_index([("datetime", DESCENDING)])
            await historical_collection.create_index([
                ("symbol", ASCENDING),
                ("datetime", DESCENDING)
            ])

            # Recommendation indexes
            recommendation_collection = self.db[settings.RECOMMENDATION_COLLECTION]
            await recommendation_collection.create_index([("symbol", ASCENDING)])
            await recommendation_collection.create_index([("period", DESCENDING)])
            await recommendation_collection.create_index([
                ("symbol", ASCENDING),
                ("period", DESCENDING)
            ], unique=True)

            logger.info("MongoDB indexes created successfully")
        except Exception as e:
            logger.warning(f"Index creation warning: {e}")

    async def get_historical_prices(
        self,
        symbol: str,
        days: int = 365,
        interval: str = "1day"
    ) -> List[dict]:
        """Fetch historical prices for a symbol from time series collection"""
        try:
            collection = self.db[settings.HISTORICAL_PRICE_COLLECTION]
            
            # Calculate date range
            end_date = datetime.utcnow()
            start_date = end_date - timedelta(days=days)
            
            # Query time series collection
            # datetime is stored as ISODate in time series
            cursor = collection.find({
                "symbol": symbol,
                "interval": interval,
                "datetime": {
                    "$gte": start_date,
                    "$lte": end_date
                }
            }).sort("datetime", ASCENDING)
            
            results = await cursor.to_list(length=None)
            logger.info(f"Fetched {len(results)} historical prices for {symbol}")
            return results
        except Exception as e:
            logger.error(f"Error fetching historical prices for {symbol}: {e}")
            return []

    async def save_recommendation(self, recommendation: dict) -> bool:
        """Save or update recommendation"""
        try:
            collection = self.db[settings.RECOMMENDATION_COLLECTION]
            
            # Remove created_at if exists (will be set by $setOnInsert)
            rec_data = {k: v for k, v in recommendation.items() if k != 'created_at'}
            
            # Update or insert
            result = await collection.update_one(
                {
                    "symbol": recommendation["symbol"],
                    "period": recommendation["period"]
                },
                {
                    "$set": {
                        **rec_data,
                        "updated_at": datetime.utcnow()
                    },
                    "$setOnInsert": {
                        "created_at": datetime.utcnow()
                    }
                },
                upsert=True
            )
            
            logger.info(
                f"Saved recommendation for {recommendation['symbol']} "
                f"on {recommendation['period']}"
            )
            return True
        except Exception as e:
            logger.error(f"Error saving recommendation: {e}")
            return False

    async def get_recommendation(
        self,
        symbol: str,
        period: Optional[datetime] = None
    ) -> Optional[dict]:
        """Get recommendation for a symbol"""
        try:
            collection = self.db[settings.RECOMMENDATION_COLLECTION]
            
            query = {"symbol": symbol}
            if period:
                query["period"] = period.date()
            
            result = await collection.find_one(
                query,
                sort=[("period", DESCENDING)]
            )
            
            return result
        except Exception as e:
            logger.error(f"Error fetching recommendation: {e}")
            return None

    async def get_all_symbols(self) -> List[str]:
        """Get all unique symbols from historical prices time series collection"""
        try:
            collection = self.db[settings.HISTORICAL_PRICE_COLLECTION]
            symbols = await collection.distinct("symbol")
            logger.info(f"Found {len(symbols)} unique symbols")
            return symbols
        except Exception as e:
            logger.error(f"Error fetching symbols: {e}")
            return []


# Global instance
mongodb_service = MongoDBService()
