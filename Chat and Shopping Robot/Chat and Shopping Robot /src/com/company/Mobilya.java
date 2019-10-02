package com.company;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class Mobilya extends Product {

    private String _AgacMalzemesi, _Ebatlar, _KullanimYeri;

    private Tweet UrunYorumlariMobilya;

    public Mobilya(int pid,int  GarantiSuresi, String brand, String model, String AgacMalzemesi, String Ebatlar, String KullanimYeri) {
        super(pid, GarantiSuresi, brand, model);

        MongoDB verilerDB = new MongoDB();
        String[] ilgiliDegerler = new String[10];


        //İlgili veri MongoDB veritabanında varsa oradan çekmektedir..
        if(verilerDB.MobilyaBilgileriniAl(pid,ilgiliDegerler)) {

            super.setpId(Integer.parseInt(ilgiliDegerler[0]));
            super.set_GarantiSuresi(Integer.parseInt(ilgiliDegerler[1]));
            super.setpBrand(ilgiliDegerler[2]);
            super.setpModel(ilgiliDegerler[3]);
            this._AgacMalzemesi = ilgiliDegerler[4];
            this._Ebatlar = ilgiliDegerler[5];
            this._KullanimYeri = ilgiliDegerler[6];

            verilerDB.VeritabaniniKapat();
        }else{ //mongoDB veritabanında yoksa sıfırdan yeni ürün oluşturup eklemektedir.

            _AgacMalzemesi = AgacMalzemesi;
            _Ebatlar = Ebatlar;
            _KullanimYeri = KullanimYeri;

            verilerDB.MobilyaEkle(pid,GarantiSuresi,brand,model,AgacMalzemesi,Ebatlar,KullanimYeri);
            verilerDB.VeritabaniniKapat();
        }

        UrunYorumlariMobilya = new Tweet();


        //Tweet class'ının içindeki hashtag ifadesine marka adı ve model ismini atar.
        UrunYorumlariMobilya.setHashtag(super.getIdentityDegeri());

        //Twitter yorumları, kutup degerleri ve kutup degerleri ortalamasi bu constructor'da yapıldı..
        String[] twitterYorumlari = new String[10];


        //Redis DAO suna erişim RedisSingleton patterniyle sağlanmıştır..
        Jedis jedis = RedisSingleton.getInstance().RedisOkumaNesnesiniGetir();


        List<String> urunlerim = new ArrayList<>();

        urunlerim.addAll(jedis.lrange(super.getIdentityDegeri(), 0 ,9));

        //Eğer ilgili string idendity'sinin yani o ürün hakkında bir yorum yoksa yorum oluştur ve redis.io ya ekle..
        if(urunlerim.size() == 0) {

            //O keye ait veriler sıfırlandı.
            try {
                jedis.del(super.getIdentityDegeri());
                jedis.sync();
            }catch (Exception e){
               // System.out.println("Silme işlemi başarısız.. : " + e);
            }

            //Twitter yorumları, kutup degerleri ve kutup degerleri ortalamasi bu constructor'da yapıldı..
            Fonksiyonlarim.TwitterYorumuAta(brand, twitterYorumlari);
            UrunYorumlariMobilya.setYorum(twitterYorumlari);

            for (int i=0 ;i<twitterYorumlari.length; i++){
                if(twitterYorumlari[i] != null){
                    try {
                        jedis.lpush(super.getIdentityDegeri(), twitterYorumlari[i]);
                        jedis.sync();
                    }catch (Exception e){
                     //   System.out.println("Ekleme işlemi başarısız.. : " + e + "\n");
                    }
                }

            }


        }
        //Eğer ürün hakkında databasede yorum varsa, o yorumlari database'den çek.
        else{
            for(int i = 0 ; i<urunlerim.size();i++)
                twitterYorumlari[i] = urunlerim.get(i);

            UrunYorumlariMobilya.setYorum(twitterYorumlari);

        }

        jedis.close();

        float urunKutupDegeri = 0;

        // İlgili yorumun puan değerini döndürür.

        //Strateji deseni ile oluşturulmuş yapının içinde ikinci hesaplama yöntemi kullanıldı..
        ContextStrategyPattern contextStrategyPattern = new ContextStrategyPattern(new StrategyDeseniDosyadanPuanlaIkinciYontem());

        for (int i = 0; i < UrunYorumlariMobilya.getYorum().length; i++)
            if (UrunYorumlariMobilya.getYorum()[i] != null)
                urunKutupDegeri += contextStrategyPattern.executeStrategy(UrunYorumlariMobilya.getYorum()[i]);



        UrunYorumlariMobilya.setKutupDegeri(urunKutupDegeri / UrunYorumlariMobilya.getYorum().length);

    }

    public Tweet getUrunYorumlariMobilya() {
        return UrunYorumlariMobilya;
    }

    public void setUrunYorumlariMobilya(Tweet urunYorumlariMobilya) {
        UrunYorumlariMobilya = urunYorumlariMobilya;
    }

    public String get_AgacMalzemesi() {
        return _AgacMalzemesi;
    }

    public void set_AgacMalzemesi(String _AgacMalzemesi) {
        this._AgacMalzemesi = _AgacMalzemesi;
    }

    public String get_Ebatlar() {
        return _Ebatlar;
    }

    public void set_Ebatlar(String _Ebatlar) {
        this._Ebatlar = _Ebatlar;
    }

    public String get_KullanimYeri() {
        return _KullanimYeri;
    }

    public void set_KullanimYeri(String _KullanimYeri) {
        this._KullanimYeri = _KullanimYeri;
    }
}
