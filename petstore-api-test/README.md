# Petstore API Test Projesi

Bu proje, [Swagger Petstore API](https://petstore.swagger.io/v2) için kapsamlı CRUD (Create, Read, Update, Delete) işlemlerini test eden bir Java test framework'üdür.

## 🚀 Proje Özeti

Petstore API Test projesi, Spring Boot ve WebClient kullanarak reaktif programlama yaklaşımı ile Petstore API'sinin işlevselliğini doğrular. Proje, hayvan mağazası yönetim sisteminin temel işlemlerini test eder.

## 🛠️ Teknolojiler

- **Java 21** - Programlama dili
- **Spring Boot** - Ana framework
- **WebClient** - Reaktif HTTP client
- **JUnit Jupiter 5.10.0** - Test framework
- **AssertJ** - Gelişmiş assertion library
- **Gradle** - Build tool

## 📋 Test Senaryoları

Proje aşağıdaki test senaryolarını kapsar:

### ✅ Temel CRUD Operasyonları
- **Pet Ekleme** - Yeni hayvan kaydı oluşturma
- **Pet Güncelleme** - Mevcut hayvan bilgilerini güncelleme
- **Pet Getirme** - ID ile hayvan bilgisi sorgulama
- **Pet Silme** - Hayvan kaydını sisteme silme

### 🔍 Arama Operasyonları
- **Status'a Göre Arama** - Hayvan durumuna göre filtreleme

### ⚠️ Hata Senaryoları
- Geçersiz Pet ID ile işlem yapma
- Olmayan Pet'i silmeye çalışma
- Negatif ID ile sorgulama
- Geçersiz status ile arama
- Boş isimle Pet oluşturma
- Çok uzun isimle Pet oluşturma

```

## 🚀 Kurulum ve Çalıştırma

### Gereksinimler
- Java 21 veya üzeri
- Gradle 7.0 veya üzeri

### Kurulum
```bash
# Projeyi klonlayın
git clone <repository-url>
cd petstore-api-test

# Bağımlılıkları yükleyin
./gradlew build
```

### Testleri Çalıştırma
```bash
# Tüm testleri çalıştır
./gradlew test

# Belirli bir test sınıfını çalıştır
./gradlew test --tests PetStoreCrudTest

# Test raporunu görüntüle
./gradlew test --info
```

## 🏗️ API Endpoint'leri

Proje aşağıdaki Petstore API endpoint'lerini test eder:

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| POST | `/pet` | Yeni pet ekleme |
| GET | `/pet/{petId}` | Pet ID ile getirme |
| PUT | `/pet` | Pet bilgilerini güncelleme |
| DELETE | `/pet/{petId}` | Pet silme |
| GET | `/pet/findByStatus` | Status'a göre pet arama |

## 📈 Test Kapsamı

- **Fonksiyonel Testler**: Temel CRUD operasyonları
- **Negatif Testler**: Hata durumları ve edge case'ler
- **Veri Doğrulama**: Response validasyonu
 