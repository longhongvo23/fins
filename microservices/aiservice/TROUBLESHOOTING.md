# AI Service - Hướng dẫn khắc phục sự cố

## 🔧 Các vấn đề đã fix

### ✅ 1. MongoDB Authentication Error
**Vấn đề**: `Command createIndexes requires authentication`

**Giải pháp**: Đã thêm credentials vào MONGODB_URI
```yaml
MONGODB_URI=mongodb://${STOCKSERVICE_MONGODB_USER}:${STOCKSERVICE_MONGODB_PASSWORD}@stockservice-mongodb:27017/stockservice?authSource=admin
```

### ✅ 2. Kafka GroupCoordinatorNotAvailableError
**Vấn đề**: `Group Coordinator Request failed: [Error 15]`

**Nguyên nhân**: 
- Kafka topics chưa được tạo
- Consumer cố gắng subscribe vào topic không tồn tại

**Giải pháp**:
1. Mặc định disable Kafka consumer: `KAFKA_CONSUMER_ENABLED=false`
2. Tạo topics thủ công sau khi Kafka ready:
   ```bash
   cd microservices/docker-compose
   setup-kafka-topics.bat  # Windows
   ```
3. Enable consumer sau khi topics đã tạo (optional)

### ✅ 3. aiservice không hiển thị trong Consul
**Vấn đề**: Service không register với Consul

**Giải pháp**: Đã thêm Consul registration
- Auto-register khi start
- Health check endpoint
- Service metadata (tags, version, etc.)

### ✅ 4. Chưa có historical data
**Vấn đề**: System mới không có data để predict

**Giải pháp**:
- Thêm validation: minimum 30 days data required
- Trả về error message rõ ràng
- `/api/symbols` endpoint hiển thị data statistics
- Disable auto-prediction mặc định: `AUTO_PREDICTION_ENABLED=false`

## 🚀 Quy trình khởi động đúng

### Bước 1: Start containers
```bash
cd microservices/docker-compose
docker-compose up -d --build
```

### Bước 2: Đợi services ready (30-60s)
```bash
# Kiểm tra services đang chạy
docker-compose ps

# Xem logs
docker-compose logs -f
```

### Bước 3: Tạo Kafka topics
```bash
# Sau khi Kafka đã ready (xem logs không còn error)
setup-kafka-topics.bat  # Windows
./setup-kafka-topics.sh  # Linux/Mac
```

### Bước 4: Verify Kafka topics
```bash
docker exec kafka kafka-topics --list --bootstrap-server localhost:9092

# Kết quả mong đợi:
# stock.updates
# predictions.recommendations
```

### Bước 5: Check Consul
```bash
# Mở browser
http://localhost:8500

# Kiểm tra Services tab
# aiservice sẽ hiển thị với:
# - Status: passing (green)
# - Tags: ai, prediction, prophet, python
```

### Bước 6: Test aiservice
```bash
# Health check
curl http://localhost:8086/health

# Kết quả mong đợi:
{
  "status": "healthy",
  "service": "aiservice",
  "version": "1.0.0",
  "dependencies": {
    "mongodb": "healthy",
    "kafka": "healthy"
  }
}

# Kiểm tra symbols available
curl http://localhost:8086/api/symbols
```

### Bước 7: Đợi crawlservice thu thập data
```bash
# Xem crawlservice logs
docker-compose logs -f crawlservice

# Kiểm tra data trong MongoDB
docker exec -it stockservice-mongodb mongosh \
  -u stockservicelong -p stockservice26012003 \
  --authenticationDatabase admin

use stockservice
db.historical_price.countDocuments()
```

### Bước 8: Test prediction (sau khi có đủ data)
```bash
# Kiểm tra symbol nào có đủ data
curl http://localhost:8086/api/symbols

# Test prediction
curl -X POST http://localhost:8086/api/predict \
  -H "Content-Type: application/json" \
  -d '{"symbol": "AAPL", "forecast_days": 30}'
```

## 📊 Monitoring

### Kiểm tra logs realtime
```bash
# Tất cả services
docker-compose logs -f

# Chỉ aiservice
docker-compose logs -f aiservice

# Filter by keyword
docker-compose logs aiservice | grep -i error
docker-compose logs aiservice | grep -i kafka
docker-compose logs aiservice | grep -i consul
```

### Kiểm tra MongoDB
```bash
# Connect to MongoDB
docker exec -it stockservice-mongodb mongosh \
  -u stockservicelong -p stockservice26012003 \
  --authenticationDatabase admin

# Check databases
show dbs

# Use stockservice
use stockservice

# Check collections
show collections

# Count documents
db.historical_price.countDocuments()
db.recommendation.countDocuments()

# Check latest recommendations
db.recommendation.find().sort({period: -1}).limit(5).pretty()

# Check data per symbol
db.historical_price.aggregate([
  { $group: { _id: "$symbol", count: { $sum: 1 } } },
  { $sort: { count: -1 } },
  { $limit: 20 }
])
```

### Kiểm tra Kafka
```bash
# List topics
docker exec kafka kafka-topics --list --bootstrap-server localhost:9092

# Describe topic
docker exec kafka kafka-topics \
  --describe \
  --topic stock.updates \
  --bootstrap-server localhost:9092

# Check consumer groups
docker exec kafka kafka-consumer-groups \
  --list \
  --bootstrap-server localhost:9092

# Check consumer group details
docker exec kafka kafka-consumer-groups \
  --describe \
  --group aiservice-consumer \
  --bootstrap-server localhost:9092
```

## 🔄 Enable Kafka Consumer (Optional)

Sau khi đã có Kafka topics và muốn enable real-time processing:

### Cách 1: Qua docker-compose.yml
```yaml
# Sửa trong docker-compose.yml
environment:
  - KAFKA_CONSUMER_ENABLED=true
```

Restart service:
```bash
docker-compose restart aiservice
```

### Cách 2: Environment variable
```bash
docker-compose stop aiservice
docker-compose up -d aiservice -e KAFKA_CONSUMER_ENABLED=true
```

### Kiểm tra consumer đã chạy
```bash
# Xem logs
docker-compose logs -f aiservice

# Kết quả mong đợi:
# Kafka consumer is enabled, starting...
# Kafka consumer started for topic: stock.updates
```

## ⚠️ Lưu ý quan trọng

### 1. Data Requirements
- **Minimum**: 30 ngày historical data cho mỗi symbol
- **Recommended**: 90-365 ngày để có prediction chính xác hơn
- Crawlservice cần chạy một thời gian để thu thập đủ data

### 2. Resource Requirements
- **RAM**: Minimum 2GB cho aiservice container
- **CPU**: 2 cores recommended (Prophet training cần CPU)
- **Disk**: Tùy thuộc MongoDB data size

### 3. Performance
- First prediction cho symbol: 5-30s (train model)
- Subsequent predictions: Nhanh hơn nếu cache
- Batch predictions: Chạy background, không block

### 4. Production Recommendations
- Enable auto-prediction sau khi có đủ data
- Set up monitoring và alerts
- Configure proper resource limits
- Enable Kafka consumer khi hệ thống đã stable
- Backup MongoDB recommendation collection

## 🆘 Common Issues

### Issue: "Unable to generate prediction - Insufficient data"
```bash
# Kiểm tra data available
curl http://localhost:8086/api/symbols

# Đợi crawlservice crawl thêm data
# Hoặc trigger manual crawl (nếu có endpoint)
```

### Issue: Container keeps restarting
```bash
# Check logs
docker-compose logs aiservice

# Common causes:
# 1. MongoDB connection failed -> Check credentials
# 2. Port conflict -> Change APP_PORT
# 3. Out of memory -> Increase Docker memory limit
```

### Issue: Prediction very slow
```bash
# Prophet training lần đầu mất thời gian
# Nếu quá chậm:
# 1. Giảm số ngày train data (365 -> 180)
# 2. Tăng CPU allocation
# 3. Consider caching trained models (future improvement)
```

## 📝 Configuration Reference

### Environment Variables
```bash
# Required
MONGODB_URI=mongodb://user:pass@host:port/db?authSource=admin
KAFKA_BOOTSTRAP_SERVERS=kafka:9092

# Optional
KAFKA_CONSUMER_ENABLED=false        # true to enable real-time processing
AUTO_PREDICTION_ENABLED=false       # true to enable daily auto-predictions
SERVICE_DISCOVERY_ENABLED=true      # false to disable Consul registration
LOG_LEVEL=INFO                      # DEBUG for more logs

# Thresholds (customize as needed)
STRONG_BUY_THRESHOLD=10.0
BUY_THRESHOLD=5.0
HOLD_THRESHOLD=2.0
SELL_THRESHOLD=-5.0
STRONG_SELL_THRESHOLD=-10.0
```

### Default Ports
- **8086**: aiservice HTTP API
- **27017**: MongoDB (internal)
- **9092**: Kafka (internal)
- **8500**: Consul UI

## ✅ Verification Checklist

- [ ] All containers running: `docker-compose ps`
- [ ] MongoDB accessible with auth
- [ ] Kafka topics created
- [ ] aiservice registered in Consul
- [ ] Health endpoint returns 200: `curl http://localhost:8086/health`
- [ ] API docs accessible: `http://localhost:8086/docs`
- [ ] Crawlservice collecting data
- [ ] At least one symbol has 30+ days data
- [ ] Test prediction works for that symbol

## 🎯 Next Steps

1. **Monitor data collection**: Đợi crawlservice thu thập đủ data (30+ ngày)
2. **Test predictions**: Thử predict với symbols có đủ data
3. **Enable auto-prediction**: Set `AUTO_PREDICTION_ENABLED=true` khi ready
4. **Enable Kafka consumer**: Set `KAFKA_CONSUMER_ENABLED=true` cho real-time
5. **Set up monitoring**: Grafana dashboards cho metrics
6. **Production tuning**: Optimize thresholds và parameters
