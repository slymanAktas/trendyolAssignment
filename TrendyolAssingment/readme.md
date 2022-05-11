## Bölüm 1 ##

    1. '/src/test/java/ui/BoutiqueTest' class'ı içerisindeki 'shouldWriteBoutiqueLinksToCsv' testi içerisinde  ana sayfadaki 
       tüm butik linkleri ve bu linklere request atıldığında dönen response code'lar 'boutiquesResponseCodes.csv' dosyasına 
       kaydediliyor.
   
    2. '/src/test/java/ui/BoutiqueTest' class'ı içerisindeki 'shouldWriteBoutiquesLoadingTimeToCvs' testi içerisinde anasayfa 
       scroll edildiğinde gelen butik image linklerinin yüklenme süreleri ve response code'ları 
       'boutiqueImagesResponseTime.csv' dosyasına kaydediliyor.
   
    3. '/src/test/java/ui/LoginTest' class'ı içerisindeki 'shouldLoginWithMultipleCredentialsAsParameterized' testi içerisinde
        kendi tasarladığım data driven login testleri yer alıyor. Test datalarına 'src/main/resources/loginCredentials.csv'
       dosyasından erişebilirsiniz.
   
Tüm testleri crossbrowser(-Dbrowser) olarak, paralel bir şekilde remote makina üzerinde(-Dremote) ayağa kaldırdığımız 
docker container'lar içerisinde koşabilmek için;
    
    . Test'lerin koşacağı remote makinaya ssh ile erişip 'docker-compose.yaml' olduğu dizin'de aşağıdaki komut ile
        testlerin koşacağı container'lar ayağa kaldırılır.
        `docker-compose -p grid up --scale chromenode=20 --force-recreate -d`
        
    . Tüm testler veya POM'da tanımladığımız profiller içerisinde tanımladığımız test suite'ler aşağıda yer alan komut
        ile execute edilir.
        - Tüm testler:
            `mvn clean verify -Dremote=true -Dbrowser=chrome -Dgrid=remoteMachineIP`
        - Spesific profil (POM'da tanımladığım içerisinde login testlerinin yer aldığı login profili)
            `mvn clean verify -Plogin -Dremote=true -Dbrowser=chrome -Dgrid=remoteMachineIP`

    . Paralel koşan test sayısı POM'da yer alan tread.count property'si ile ayarlanabilir. Junit'de her bir thread 
        testin koşulduğu makinanın gücüne bağlı olarak ortalama 10 testi paralel koşabilir.

Testler'den birinin fail olması durumunda 'src/main/java/base/BaseUITest' içerisinde yer alan test rule implementasyon'u
olan TestWatcherItems içerisinde yer alan fail hook'u içerisinde ekran görüntüsü alınıp 'screenshots' dizini içerisine 
kaydedilmektedir.

Testleri koşan Surefire plugin'inin Allure property'si test sonuçlarını 'allure-results' içerisine log'lamaktadır.
Bu log file'lardan test raporu oluşturmak için ilgili dizinde ````allure serve allure-results```` komutu çalıştırılır.
Bu komutu çalıştırabilrmek için allure bilgisayarımızda kurulu olmalıdır.(brew install allure)

Test'leri yazarken ödev'de bellirttiğiniz mimari açıdan büyümeye hazır bir class yapısı ve tasarım desenleri kullanmya
çalıştım.Bu sebep ile kod içerisinde bazı yerler gereksiz bulunabilir:)
Detaylarını konuşuruz.
  

