@echo off
REM Script to create Kafka topics for aiservice

echo Creating Kafka topics for aiservice...

REM Wait for Kafka to be ready
echo Waiting for Kafka to be ready...
timeout /t 10 /nobreak

REM Create stock.updates topic
docker exec kafka kafka-topics --create ^
  --bootstrap-server localhost:9092 ^
  --topic stock.updates ^
  --partitions 3 ^
  --replication-factor 1 ^
  --if-not-exists

echo ✅ Created topic: stock.updates

REM Create predictions.recommendations topic
docker exec kafka kafka-topics --create ^
  --bootstrap-server localhost:9092 ^
  --topic predictions.recommendations ^
  --partitions 3 ^
  --replication-factor 1 ^
  --if-not-exists

echo ✅ Created topic: predictions.recommendations

REM List all topics
echo.
echo Current Kafka topics:
docker exec kafka kafka-topics --list --bootstrap-server localhost:9092

echo.
echo ✅ Kafka topics setup complete!
pause
