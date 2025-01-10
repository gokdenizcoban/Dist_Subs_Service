# Dağıtık Abone Yönetim Sistemi

Bu proje, dağıtık mimaride çalışan, hata toleranslı bir abone yönetim sistemidir. Sistem, birden fazla sunucu arasında senkronizasyon sağlayarak abonelerin yönetimini gerçekleştirir.

## Özellikler

- Çoklu sunucu desteği (Server1, Server2, Server3)
- Hata toleransı ve otomatik senkronizasyon
- Protocol Buffers ile veri serileştirme
- Admin paneli ile sunucu yönetimi
- Yük dengeleme ve otomatik yönlendirme

## Sistem Gereksinimleri

- Java 11 veya üzeri
- Ruby 2.7 veya üzeri (Admin panel için)
- Protocol Buffers
- Maven

## Kurulum

1. Protocol Buffers dosyalarını derleyin:
 bash
protoc --java_out=src/main/java protobufs/.proto

2. Projeyi derleyin:
bash
mvn clean package

## Çalıştırma

1. Sunucuları başlatın:
  bash
Terminal 1
java -cp target/distributed-subscription-1.0-SNAPSHOT-jar-with-dependencies.jar dist_servers.Server1
Terminal 2
java -cp target/distributed-subscription-1.0-SNAPSHOT-jar-with-dependencies.jar dist_servers.Server2
Terminal 3
java -cp target/distributed-subscription-1.0-SNAPSHOT-jar-with-dependencies.jar dist_servers.Server3

2. Admin panelini başlatın:
   bash
cd panel
ruby admin.rb

3. Test istemcisini çalıştırın:
   bash
java -cp target/distributed-subscription-1.0-SNAPSHOT-jar-with-dependencies.jar Clients.TestClient

## Sistem Mimarisi

### Sunucular
- Her sunucu bağımsız çalışır (6001, 6002, 6003 portları)
- Admin portları: 7001, 7002, 7003
- Peer portları: 5001, 5002, 5003

### Protokol
- Protocol Buffers ile tanımlı mesajlar
- Subscriber.proto: Abone bilgileri
- Message.proto: Sunucular arası iletişim
- Configuration.proto: Sistem yapılandırması
- Capacity.proto: Sunucu kapasite bilgileri

### Hata Toleransı
- Çoklu sunucu desteği
- Otomatik senkronizasyon
- Sunucu hatası durumunda otomatik yönlendirme

## Kullanım

1. Admin panel ile hata tolerans seviyesini ayarlayın
2. Test istemcisi ile aboneleri ekleyin
3. Sunucular otomatik olarak senkronize olacaktır

## Geliştirme

Yeni özellikler eklemek için:
1. Protocol Buffers tanımlarını güncelleyin
2. İlgili Java sınıflarını oluşturun
3. Sunucu ve istemci kodlarını güncel

### Ekip üyeleri

- 22060350, Gökdeniz Çoban
- 22060399, Yavuz Selimhan Yılmaz
- 22060353, Batuhan Yılmaz
- 22060368, Deniz Çelik
