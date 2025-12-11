## ⚠️ Common Build Issues on Windows

## Vấn đề 1: Maven Wrapper Error

### Triệu chứng

Khi clone dự án về máy Windows và chạy:
```bash
./mvnw clean package -Pprod verify jib:dockerBuild -DskipTests
```

Gặp lỗi:
```
distributionUrl is not valid, must match *-bin.zip or maven-mvnd-*.zip
```

## Nguyên nhân

Git trên Windows tự động chuyển đổi line endings từ LF → CRLF, làm hỏng file `maven-wrapper.properties`. URL bị ngắt dòng và Maven Wrapper không đọc được.

## Giải pháp

### Cách 1: Reset Line Endings (Khuyến nghị)

Sau khi clone, chạy các lệnh sau để force Git dùng LF:

```bash
cd fins

# Xóa cache và reset lại tất cả file
git rm --cached -r .
git reset --hard HEAD

# Kiểm tra file đã đúng chưa
cat microservices/gateway/.mvn/wrapper/maven-wrapper.properties | tail -1
# Phải thấy dòng URL đầy đủ: distributionUrl=https://repo.maven.apache.org/.../-bin.zip
```

### Cách 2: Cấu hình Git (Một lần cho tất cả dự án)

```bash
# Tắt tự động chuyển đổi line endings
git config --global core.autocrlf input

# Sau đó clone lại dự án
cd ..
rm -rf fins
git clone https://github.com/longhongvo23/fins.git
cd fins
```

### Cách 3: Sử dụng WSL (Windows Subsystem for Linux)

Nếu có WSL, nên build trong môi trường Linux:

```bash
# Trong WSL
cd /mnt/d/DATN/fins/microservices/gateway
./mvnw clean package -Pprod verify jib:dockerBuild -DskipTests
```

## Kiểm tra

Sau khi fix, chạy lệnh test:

```bash
cd microservices/gateway
./mvnw --version
```

Nếu thấy:
```
Apache Maven 3.9.9 ...
```
→ ✅ Đã fix thành công!

## Ngăn chặn vấn đề này

File `.gitattributes` đã được thêm vào dự án để force LF cho tất cả file `.properties`. Nếu vẫn gặp lỗi, hãy:

1. Cập nhật Git lên phiên bản mới nhất
2. Đảm bảo clone với `git config core.autocrlf input`
3. Hoặc sử dụng WSL để tránh vấn đề line endings trên Windows

---

**Lưu ý**: Các service khác (userservice, stockservice, newsservice, ...) cũng có thể gặp lỗi tương tự. Áp dụng **Cách 1** sẽ fix cho tất cả services cùng lúc.

---

## Vấn đề 2: Missing Component Files (FIXED ✅)

### Triệu chứng

Build bị lỗi:
```
Error: Module not found: Error: Can't resolve './logs/logs.component'
Cannot find module './logs/logs.component' or its corresponding type declarations
```

### Nguyên nhân

File `.gitignore` trước đó có dòng `logs/` làm Git ignore toàn bộ thư mục `logs`, bao gồm cả source code components trong `src/main/webapp/app/admin/logs/`.

### Trạng thái

✅ **Đã fix trong commit `bae9298`**:
- Cập nhật `.gitignore` để chỉ ignore file log (*.log), không ignore thư mục source code
- Đã thêm tất cả file logs component vào Git
- Pull code mới nhất sẽ có đầy đủ file

### Kiểm tra

Sau khi pull code mới:
```bash
cd microservices/gateway
ls -la src/main/webapp/app/admin/logs/

# Phải thấy các file:
# - logs.component.ts
# - logs.component.html
# - logs.service.ts
# - log.model.ts
```

Nếu vẫn thiếu file, chạy:
```bash
git pull origin master
git checkout -- .
```
