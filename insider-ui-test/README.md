# Insider UI Test Projesi

## 📋 Proje Hakkında

Bu proje, **Insider şirketinin kariyer sayfası** için otomatik UI testleri gerçekleştiren bir **Spring Boot** uygulamasıdır. **Selenium WebDriver** kullanarak **Page Object Model (POM)** tasarım deseni ile geliştirilmiş ve **AI destekli test otomasyon** özellikleri içermektedir.

## 🛠️ Teknolojiler

- **Java** (Spring Boot 3.2.0)
- **Selenium WebDriver** (4.15.0)
- **WebDriverManager** (5.6.2)
- **JUnit 5** (5.10.0)
- **Lombok** (1.18.30)
- **Gradle** (Build Tool)
- **Google Genai** (AI/LLM Entegrasyonu)

## 🤖 AI Destekli Test Otomasyon Özellikleri

### AIFactory Interface
Proje, **doğal dil işleme** ve **AI destekli test otomasyon** için `AIFactory` interface'ini kullanmaktadır:

#### Temel Özellikler:
- **🧠 LLM Entegrasyonu**: Google Gemini 2.0 Flash modeli ile doğal dil komutlarını test aksiyonlarına dönüştürme
- **🌳 DOM Analizi**: Sayfa elementlerini otomatik analiz etme ve interaktif elementleri tespit etme
- **📝 Doğal Dil Testleri**: "Login sayfasına git ve kullanıcı adı gir" gibi doğal dil komutlarıyla test yazma
- **🎯 Otomatik Aksiyon Çalıştırma**: AI'ın önerdiği aksiyonları otomatik olarak Selenium ile çalıştırma

#### Kullanım Örneği:
```java
// Doğal dil komutu ile test çalıştırma
homePage.ai("Login formunu doldur ve giriş yap");

// Belirli bir sayfa sınıfı ile AI kullanımı
LoginPage loginPage = homePage.ai(LoginPage.class, "Kullanıcı girişi yap");
```

#### Teknik Detaylar:
- **DOM Tree Çıkarma**: JavaScript ile sayfa elementlerini analiz etme
- **JSON Response**: AI'dan gelen yanıtları structured format'ta işleme
- **XPath Tabanlı**: Element lokasyonu için XPath kullanımı
- **Environment Variables**: `GEMINI_API_KEY` 

## 📁 Proje Yapısı

```
src/
├── main/java/com/insider/
│   ├── annotations/          # Test anotasyonları
│   ├── configuration/        # Konfigürasyon sınıfları
│   ├── context/             # Application context yönetimi
│   ├── cookie/              # Cookie yönetimi
│   ├── driver/              # WebDriver yönetimi
│   ├── extensions/          # JUnit extensions
│   ├── page/                # Page Object Model sınıfları
│   ├── provider/            # Bean provider sınıfları
│   └── WebUiTestApplication.java
├── main/resources/
│   ├── application.yml      # Uygulama konfigürasyonu
│   └── banner.txt          # Uygulama banner'ı
└── test/java/com/insider/
    ├── AbstractTestData.java
    └── InsiderCareersQAJobListingsTest.java
```

## 🚀 Başlangıç

### Gereksinimler

- **Java 21** veya üzeri
- **Gradle 7.0** veya üzeri

### Kurulum

1. Projeyi klonlayın:
```bash
git clone <repository-url>
cd insider-ui-test
```

2. Bağımlılıkları yükleyin:
```bash
./gradlew build
```

### Testleri Çalıştırma

Tüm testleri çalıştırmak için:
```bash
./gradlew test
```

Belirli bir test sınıfını çalıştırmak için:
```bash
./gradlew test --tests InsiderCareersQAJobListingsTest
```

Testleri profil ile çalıştırmak için:
```bash
./gradlew test -Dspring.profiles.active=test
```

## 🧪 Test Senaryoları

### 1. Kariyer Sayfası Navigasyonu ve İş İlanları Yükleme Testi
- **Amaç**: Navbar'dan Careers sayfasına geçiş yapma ve "Load More" fonksiyonunu test etme
- **Adımlar**:
  - Ana sayfadan Company → Careers menüsüne tıklama
  - URL ve lokasyon bilgilerini doğrulama
  - "Load More" butonu ile iş ilanları yükleme
  - Slider aktiflik kontrolü

### 2. Quality Assurance İşleri Filtreleme ve Lever Yönlendirme Testi
- **Amaç**: QA pozisyonlarını filtreleme ve başvuru sayfasına yönlendirmeyi test etme
- **Adımlar**:
  - QA careers sayfasına gitme
  - "See all QA jobs" butonuna tıklama
  - İstanbul, Türkiye lokasyonu ve QA departmanı ile filtreleme
  - Pozisyon bilgilerini doğrulama
  - "View Role" ile Lever sayfasına yönlendirme kontrolü
