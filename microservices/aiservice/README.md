# AI Service - Stock Price Prediction using Prophet

AI Service sử dụng Prophet (Meta/Facebook) để dự đoán giá cổ phiếu và tạo recommendations cho hệ thống Stock App.

## 🎯 Tính năng

- **Dự đoán giá cổ phiếu** sử dụng Prophet machine learning model
- **Tự động tạo recommendations** dựa trên kết quả dự đoán
- **RESTful API** với FastAPI framework
- **Kafka integration** để nhận stock updates và publish predictions
- **MongoDB integration** để đọc historical prices và lưu recommendations
- **Scheduled jobs** chạy dự đoán tự động theo lịch (mặc định: hàng ngày lúc 00:00)
- **Docker support** để dễ dàng deployment
- **Health checks** và monitoring

## 🏗️ Kiến trúc

```
aiservice/
├── app/
│   ├── __init__.py
│   ├── main.py              # FastAPI application
│   ├── config.py            # Configuration settings
│   ├── models.py            # Pydantic models
│   ├── database.py          # MongoDB service
│   ├── prophet_service.py   # Prophet prediction logic
│   ├── kafka_service.py     # Kafka producer/consumer
│   └── scheduler.py         # APScheduler jobs
├── Dockerfile
├── docker-compose.yml       # Standalone testing
├── requirements.txt
├── .env
└── README.md
```

## 📋 Yêu cầu

- Python 3.11+
- MongoDB (stockservice database)
- Kafka
- Docker & Docker Compose (optional)

## 🚀 Cài đặt

### Cách duy nhất - Chạy một phát với Docker Compose ⚡

```bash
cd microservices/docker-compose

# Chạy tất cả services
docker compose up -d --build

# DONE! ✅ Tất cả mọi thứ tự động:
# ✅ Kafka auto-create topics khi producer/consumer connect
# ✅ Consumer tự động retry cho đến khi Kafka ready  
# ✅ Predictions tự động skip nếu chưa đủ data
# ✅ Service tự động register với Consul
# ✅ Scheduler chạy nhưng skip gracefully nếu chưa có data

# Kiểm tra logs
docker compose logs -f aiservice

# Kiểm tra health
curl http://localhost:8086/health

# Kiểm tra Consul UI
# Browser: http://localhost:8500
```

### Monitoring Progress

```bash
# Xem aiservice đang làm gì
docker compose logs -f aiservice

# Kiểm tra crawlservice đang thu thập data
docker compose logs -f crawlservice

# Kiểm tra có bao nhiêu data
curl http://localhost:8086/api/symbols

# Test prediction (sẽ skip nếu chưa đủ data)
curl -X POST http://localhost:8086/api/predict \
  -H "Content-Type: application/json" \
  -d '{"symbol": "AAPL", "forecast_days": 30}'
```

### Quy trình tự động

1. **Khởi động** (`docker compose up -d`):
   - ✅ Tất cả services start
   - ✅ MongoDB connect với authentication
   - ✅ Kafka producer start (retry đến khi thành công)
   - ✅ Kafka consumer start (retry, topics tự động tạo)
   - ✅ Consul registration
   - ✅ Scheduler start

2. **Đợi data** (tự động):
   - 🔄 Crawlservice thu thập historical data
   - ⏭️ Predictions skip nếu chưa đủ 30 ngày data
   - 📝 Logs rõ ràng: "Waiting for crawlservice to collect more data"

3. **Khi có đủ data** (tự động):
   - ✅ Predictions tự động chạy cho symbols có đủ data
   - ✅ Recommendations lưu vào MongoDB
   - ✅ Events publish qua Kafka
   - ✅ Scheduler chạy daily predictions

**KHÔNG cần**:
- ❌ Chạy scripts thủ công
- ❌ Tạo Kafka topics manually
- ❌ Enable/disable features
- ❌ Restart services
- ❌ Đợi rồi mới start

**Chỉ cần**: `docker compose up -d` → XONG! 🎉

## 🔧 Cấu hình

Tất cả được config sẵn trong `docker-compose.yml`, không cần chỉnh sửa:

```env
# Application
APP_PORT=8086                        # Port riêng biệt, không xung đột

# MongoDB (auto-configured)
MONGODB_URI=mongodb://user:pass@host:port/db?authSource=admin

# Kafka (auto-configured)
KAFKA_BOOTSTRAP_SERVERS=kafka:9092

# Features (all enabled by default)
SERVICE_DISCOVERY_ENABLED=true      # Auto-register với Consul
AUTO_PREDICTION_ENABLED=true        # Daily predictions (skip nếu thiếu data)

# Smart Defaults
# - Kafka auto-create topics ✅
# - Consumer auto-retry ✅  
# - Predictions skip gracefully nếu thiếu data ✅
# - All features enabled, zero manual steps ✅
```

### Tùy chỉnh Thresholds (optional)

Nếu muốn thay đổi ngưỡng recommendation, chỉnh trong `.env`:

```env
STRONG_BUY_THRESHOLD=10.0        # >= 10% tăng
BUY_THRESHOLD=5.0                # >= 5% tăng
HOLD_THRESHOLD=2.0               # -2% đến 2%
SELL_THRESHOLD=-5.0              # <= -5% giảm
STRONG_SELL_THRESHOLD=-10.0      # <= -10% giảm
```

## 📡 API Endpoints

### Health Check
```bash
GET /health
# Response: Service health status
```

### Dự đoán đơn lẻ
```bash
POST /api/predict
Content-Type: application/json

{
  "symbol": "AAPL",
  "forecast_days": 30
}
```

### Dự đoán hàng loạt (background)
```bash
POST /api/predict/batch
Content-Type: application/json

{
  "symbols": ["AAPL", "GOOGL", "MSFT"],
  "forecast_days": 30
}
```

### Tạo recommendation
```bash
POST /api/recommendation/generate
Content-Type: application/json

{
  "symbol": "AAPL",
  "forecast_days": 30
}
```

### Lấy recommendation
```bash
GET /api/recommendation/{symbol}
# Example: GET /api/recommendation/AAPL
```

### Lấy danh sách symbols
```bash
GET /api/symbols
```

### Trigger manual prediction (Admin)
```bash
POST /api/admin/run-predictions
```

## 🧪 Testing

```bash
# Test health endpoint
curl http://localhost:8086/health

# Test prediction
curl -X POST http://localhost:8086/api/predict \
  -H "Content-Type: application/json" \
  -d '{"symbol": "AAPL", "forecast_days": 30}'

# Test get recommendation
curl http://localhost:8086/api/recommendation/AAPL
```

## 🔄 Workflow (Hoàn toàn tự động)

1. **`docker compose up -d`** - Start tất cả services
2. **Auto-connect** - MongoDB, Kafka với retry logic
3. **Auto-register** - Consul service discovery
4. **Auto-create** - Kafka topics khi producer/consumer connect
5. **Crawlservice** thu thập historical data
6. **Wait gracefully** - Predictions skip nếu chưa đủ data
7. **Auto-predict** - Khi có đủ data (30+ ngày)
8. **Auto-save** - Recommendations vào MongoDB
9. **Auto-publish** - Events qua Kafka
10. **Daily schedule** - Batch predictions (skip nếu thiếu data)

**Không cần can thiệp thủ công!** Mọi thứ tự xử lý. 🚀

## 📊 Recommendation Logic

Service tính toán recommendation counts (giả lập 100 analysts) dựa trên % thay đổi dự đoán:

- **STRONG_BUY**: Dự đoán tăng >= 10%
- **BUY**: Dự đoán tăng >= 5%
- **HOLD**: Dự đoán thay đổi trong khoảng -2% đến +2%
- **SELL**: Dự đoán giảm >= 5%
- **STRONG_SELL**: Dự đoán giảm >= 10%

Kết quả được lưu vào collection `recommendation`:
```javascript
{
  symbol: "AAPL",
  period: ISODate("2025-01-10"),
  strongBuy: 60,
  buy: 25,
  hold: 15,
  sell: 0,
  strongSell: 0,
  metadata: {
    predicted_price: 185.50,
    current_price: 170.00,
    change_percent: 9.12
  }
}
```

## 🐛 Troubleshooting

### ❌ Service không start
```bash
# Xem logs chi tiết
docker compose logs -f aiservice

# Thường gặp:
# 1. MongoDB credentials sai → Check .env file
# 2. Port conflict → Đổi APP_PORT
# 3. Out of memory → Tăng Docker memory
```

### ⏭️ "Skipping predictions - insufficient data"
```bash
# ĐÂY LÀ BÌNH THƯỜNG! Không phải lỗi.
# Service đang đợi crawlservice thu thập data.

# Kiểm tra progress:
curl http://localhost:8086/api/symbols

# Đợi crawlservice chạy một thời gian
docker compose logs -f crawlservice

# Khi có đủ data, predictions tự động chạy
```

### 🔌 "Kafka connection error"
```bash
# Service TỰ ĐỘNG RETRY, không cần làm gì!
# Logs sẽ hiển thị: "Retrying in 5s..."

# Nếu vẫn lỗi sau nhiều retry:
docker compose ps kafka  # Check Kafka running
docker compose restart kafka
```

### 📊 "No symbols found"
```bash
# Crawlservice chưa crawl data
# Đợi một lúc hoặc check crawlservice logs

docker compose logs -f crawlservice
```

### 🏥 Health check failed
```bash
# Thường do dependencies chưa ready
# Service sẽ tự động retry và recover

# Force restart nếu cần:
docker compose restart aiservice
```

**Lưu ý**: Hầu hết "lỗi" là trạng thái đang đợi (waiting for data/kafka/etc), không phải lỗi thật. Service tự động handle và recover! ✅

## 📝 Logs

```bash
# View logs
docker compose logs -f aiservice

# Logs có emoji và màu sắc rõ ràng:
# ✅ Success: Connected, Started, Published
# ⏭️  Skip: Insufficient data (không phải lỗi!)
# 🔄 Retry: Kafka connecting...
# ❌ Error: Thật sự có vấn đề
# 📅 Scheduled: Daily jobs
# 🎧 Listening: Kafka consumer
# 📨 Received: Stock updates
```

## 🚢 Production Ready

Service đã được thiết kế production-ready:

- ✅ **Zero manual steps**: `docker compose up -d` → DONE
- ✅ **Auto-retry**: Kafka, MongoDB connections
- ✅ **Graceful degradation**: Skip nếu thiếu data, không crash
- ✅ **Health checks**: Endpoint `/health` cho monitoring
- ✅ **Service discovery**: Auto-register với Consul
- ✅ **Smart logging**: Rõ ràng phân biệt info/warning/error
- ✅ **Resource efficient**: Chỉ xử lý khi có data
- ✅ **Fault tolerant**: Continue working nếu Kafka/Consul down

**Không cần**:
- ❌ Manual topic creation
- ❌ Manual service restart  
- ❌ Enable/disable features
- ❌ Chờ đợi rồi mới start
- ❌ Scripts thủ công

**Chỉ cần**: Start và quên đi! Service tự lo mọi thứ. 🎯
# Level: INFO, DEBUG, WARNING, ERROR
# Cấu hình LOG_LEVEL trong .env
```

## 🚢 Deployment

### Production Checklist

- [ ] Đổi `LOG_LEVEL=WARNING` hoặc `ERROR`
- [ ] Cấu hình đúng MONGODB_URI production
- [ ] Cấu hình đúng KAFKA_BOOTSTRAP_SERVERS production
- [ ] Set `AUTO_PREDICTION_ENABLED=true` nếu muốn auto schedule
- [ ] Điều chỉnh `PREDICTION_CRON_SCHEDULE` phù hợp với timezone
- [ ] Cấu hình resource limits trong docker-compose
- [ ] Enable monitoring và alerts
- [ ] Backup recommendation data định kỳ

## 🔗 Integration với các services khác

### Stock Service
- Đọc historical prices từ `historical_price` collection
- Lưu recommendations vào `recommendation` collection

### Gateway
- Có thể expose AI service endpoints qua API Gateway
- Cấu hình routing trong Spring Cloud Gateway

### Kafka
- Subscribe: `stock.updates` - nhận thông báo khi có stock mới
- Publish: `predictions.recommendations` - gửi kết quả dự đoán

## 📚 Dependencies chính

- **FastAPI**: Web framework
- **Prophet**: Time series forecasting
- **Motor**: Async MongoDB driver
- **aiokafka**: Async Kafka client
- **pandas**: Data manipulation
- **APScheduler**: Job scheduling
- **loguru**: Logging
- **uvicorn**: ASGI server

## 👥 Contributing

1. Tạo branch mới: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -m 'Add some feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Tạo Pull Request

## 📄 License

Copyright © 2025 Stock App Team
