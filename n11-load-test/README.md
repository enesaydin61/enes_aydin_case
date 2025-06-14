# N11 Load Test

Bu proje N11.com sitesi için Locust kullanarak yük testi yapmak üzere hazırlanmıştır.

## Gereksinimler

- Docker
- Docker Compose

## Kurulum ve Çalıştırma

### 1. Docker Compose ile çalıştırma (Önerilen)

```bash
docker-compose up --build
```

### 2. Manuel Docker ile çalıştırma

```bash
# Docker imajını oluştur
docker build -t n11-load-test .

# Konteyneri çalıştır
docker run -p 8089:8089 n11-load-test
```

## Kullanım

1. Yukarıdaki komutlardan birini çalıştırdıktan sonra tarayıcınızdan `http://localhost:8089` adresine gidin
2. Locust web arayüzü açılacaktır
3. Test parametrelerini ayarlayın:
   - **Number of users**: Toplam kullanıcı sayısı
   - **Spawn rate**: Saniyede kaç kullanıcı eklenecek
4. "Start swarming" butonuna tıklayarak testi başlatın

## Test Senaryosu

Bu load test aşağıdaki senaryoları çalıştırır:
- Ana sayfa ziyareti
- "telefon", "bilgisayar", "spor ayakkabı" kelimeleri ile arama
- Boş arama testi