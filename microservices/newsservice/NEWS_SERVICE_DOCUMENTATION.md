# News Service - Nghiệp vụ Thu thập và Quản lý Tin tức

## Tổng quan

NewsService là microservice chịu trách nhiệm nhận, lưu trữ và quản lý tin tức tài chính cho các mã cổ phiếu. Service này nhận dữ liệu tin tức từ CrawlService và cung cấp API để truy vấn tin tức.

## Kiến trúc

### Luồng dữ liệu

```
CrawlService (Marketaux API) 
    ↓ (mỗi giờ)
    HourlyNewsScheduler
    ↓ (HTTP POST)
NewsService /api/internal/news/bulk
    ↓
NewsIngestionService
    ↓
MongoDB (company_news, news_entity collections)
```

### Các thành phần chính

#### 1. **NewsIngestionService**
- Xử lý và lưu trữ tin tức từ CrawlService
- Parse dữ liệu tin tức và entities (symbols)
- Kiểm tra duplicate dựa trên UUID
- Cung cấp query methods cho tin tức

#### 2. **InternalNewsResource**
- REST endpoints cho giao tiếp giữa microservices
- `POST /api/internal/news/bulk` - Nhận tin tức từ CrawlService
- `GET /api/internal/news/symbol/{symbol}` - Lấy tin tức theo mã
- `GET /api/internal/news/latest` - Lấy tin tức mới nhất
- `DELETE /api/internal/news/cleanup` - Xóa tin tức cũ

#### 3. **CompanyNewsResource**
- REST endpoints cho client/frontend
- `GET /api/company-news/by-symbol/{symbol}` - Tin tức theo mã
- `GET /api/company-news/latest` - Tin tức mới nhất
- Các CRUD operations chuẩn

#### 4. **NewsCleanupScheduler**
- Chạy hàng ngày lúc 00:00 (midnight)
- Tự động xóa tin tức cũ hơn 30 ngày
- Giúp tiết kiệm storage

## Data Model

### CompanyNews (MongoDB Collection: company_news)
```json
{
  "id": "string",
  "uuid": "string (unique)",
  "title": "string",
  "description": "string",
  "snippet": "string",
  "url": "string",
  "imageUrl": "string",
  "language": "string",
  "publishedAt": "Instant",
  "source": "string",
  "keywords": "string",
  "relevanceScore": "BigDecimal",
  "entities": [NewsEntity]
}
```

### NewsEntity (MongoDB Collection: news_entity)
```json
{
  "id": "string",
  "newsUuid": "string",
  "symbol": "string (e.g., AAPL)",
  "name": "string (e.g., Apple Inc.)",
  "exchange": "string (e.g., NASDAQ)",
  "country": "string",
  "type": "string",
  "industry": "string",
  "matchScore": "BigDecimal",
  "sentimentScore": "BigDecimal"
}
```

## API Endpoints

### Internal APIs (Microservice Communication)

#### Receive bulk news from CrawlService
```http
POST /api/internal/news/bulk
Content-Type: application/json

{
  "newsResponse": {
    "data": [
      {
        "uuid": "abc123",
        "title": "Apple Reports Record Earnings",
        "description": "...",
        "url": "...",
        "publishedAt": "2025-11-27T10:00:00Z",
        "source": "Reuters",
        "entities": ["AAPL|Apple Inc.|NASDAQ"],
        "relevance": 0.95
      }
    ]
  }
}

Response: 200 OK
{
  "processedCount": 10,
  "message": "Success"
}
```

#### Get news by symbol (Internal)
```http
GET /api/internal/news/symbol/AAPL?limit=20

Response: 200 OK
[CompanyNews array]
```

#### Get latest news (Internal)
```http
GET /api/internal/news/latest?limit=50

Response: 200 OK
[CompanyNews array]
```

#### Cleanup old news
```http
DELETE /api/internal/news/cleanup?daysToKeep=30

Response: 200 OK
{
  "deletedCount": 150,
  "message": "Success"
}
```

### Public APIs (Client/Frontend)

#### Get news by symbol
```http
GET /api/company-news/by-symbol/AAPL?limit=20
Authorization: Bearer <token>

Response: 200 OK
[CompanyNewsDTO array]
```

#### Get latest news
```http
GET /api/company-news/latest?limit=50
Authorization: Bearer <token>

Response: 200 OK
[CompanyNewsDTO array]
```

## Configuration

### application.yml
```yaml
spring:
  cloud:
    function:
      definition: kafkaConsumer;kafkaProducer
    stream:
      kafka:
        binder:
          replicationFactor: 1
          auto-create-topics: true
          brokers: localhost:9092
```

### Security Configuration
- Internal endpoints (`/api/internal/**`) không cần authentication
- Public endpoints (`/api/**`) yêu cầu JWT token
- Admin endpoints yêu cầu ADMIN role

## Scheduled Tasks

### NewsCleanupScheduler
- **Cron**: `0 0 0 * * ?` (Midnight daily)
- **Chức năng**: Xóa tin tức cũ hơn 30 ngày
- **Mục đích**: Tiết kiệm storage, tối ưu performance

## Dependencies

### From CrawlService
- Nhận tin tức qua HTTP POST từ HourlyNewsScheduler
- Format: NewsResponse DTO

### Database
- MongoDB cho persistence
- Collections: `company_news`, `news_entity`

## Development

### Build
```bash
cd microservices/newsservice
./mvnw clean install
```

### Run
```bash
./mvnw spring-boot:run
```

### Test
```bash
./mvnw test
```

## Monitoring

### Health Check
```http
GET /management/health
```

### Metrics
```http
GET /management/prometheus
```

### Logs
- Application logs: INFO level
- News ingestion: INFO với count
- Errors: ERROR với stack trace

## Troubleshooting

### Tin tức không được lưu
1. Kiểm tra CrawlService có chạy không
2. Kiểm tra network giữa CrawlService và NewsService
3. Xem logs của NewsIngestionService
4. Verify MongoDB connection

### Duplicate tin tức
- Service tự động kiểm tra duplicate bằng UUID
- Nếu tin tức đã tồn tại, sẽ skip và log debug

### Performance issues
- Kiểm tra số lượng tin tức trong DB
- Chạy cleanup manually nếu cần
- Tăng memory cho MongoDB nếu cần

## Future Enhancements

1. **Sentiment Analysis**: Phân tích sentiment cho mỗi tin tức
2. **Full-text Search**: Elasticsearch integration
3. **Caching**: Redis cho tin tức hot
4. **Real-time Updates**: WebSocket cho tin tức realtime
5. **News Categorization**: Tự động phân loại tin tức
6. **Duplicate Detection**: Thuật toán advanced hơn
