package com.company;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

//Product sınıfından kalıtım alır.
public class Araba extends Product {

    private String MotorHacmi, YakitCinsi, Agirlik;
    private Tweet UrunYorumlariAraba;


    public Araba(int pId, int GarantiSuresi, String pBrand, String pModel, String motorHacmi, String yakitCinsi, String agirlik) {
        super(pId, GarantiSuresi, pBrand, pModel);

        MongoDB verilerDB = new MongoDB();
        String[] ilgiliDegerler = new String[8];


        //İlgili veri MongoDB veritabanında varsa oradan çekmektedir..
        if(verilerDB.ArabaBilgileriniAl(pId,ilgiliDegerler)){

            super.setpId(Integer.parseInt(ilgiliDegerler[0]));
            super.set_GarantiSuresi(Integer.parseInt(ilgiliDegerler[1]));
            super.setpBrand(ilgiliDegerler[2]);
            super.setpModel(ilgiliDegerler[3]);
            this.MotorHacmi = ilgiliDegerler[4];
            this.YakitCinsi = ilgiliDegerler[5];
            this.Agirlik = ilgiliDegerler[6];

            verilerDB.VeritabaniniKapat();
        }
        else{ //mongoDB veritabanında yoksa sıfırdan yeni ürün oluşturup eklemektedir.

            MotorHacmi = motorHacmi;
            YakitCinsi = yakitCinsi;
            Agirlik = agirlik;

            verilerDB.ArabaEkle(pId,GarantiSuresi,pBrand,pModel,motorHacmi,yakitCinsi,agirlik);
            verilerDB.VeritabaniniKapat();
        }




        UrunYorumlariAraba = new Tweet();
        //Hashtag olarak marka adı ve model adını almaktadır.
        UrunYorumlariAraba.setHashtag(super.getIdentityDegeri());
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
            Fonksiyonlarim.TwitterYorumuAta(pBrand, twitterYorumlari);
            UrunYorumlariAraba.setYorum(twitterYorumlari);

            for (int i=0 ;i<twitterYorumlari.length; i++){
                if(twitterYorumlari[i] != null){
                    try {
                        jedis.lpush(super.getIdentityDegeri(), twitterYorumlari[i]);
                        jedis.sync();
                    }catch (Exception e){
                       // System.out.println("Ekleme işlemi başarısız.. : " + e + "\n");
                    }
                }

            }


        }
        //Eğer ürün hakkında databasede yorum varsa, o yorumlari database'den çek.
        else{
            for(int i = 0 ; i<urunlerim.size();i++)
                twitterYorumlari[i] = urunlerim.get(i);

            UrunYorumlariAraba.setYorum(twitterYorumlari);

        }

        jedis.close();

        float urunKutupDegeri = 0;

        //Strateji deseni ile oluşturulmuş yapının içinde ilk hesaplama yöntemi kullanıldı..
        ContextStrategyPattern contextStrategyPattern = new ContextStrategyPattern(new StrategyDeseniDosyadanPuanlaIlkYontem());

        for (int i = 0; i < UrunYorumlariAraba.getYorum().length; i++)
            if (UrunYorumlariAraba.getYorum()[i] != null)
                urunKutupDegeri += contextStrategyPattern.executeStrategy(UrunYorumlariAraba.getYorum()[i]);



        UrunYorumlariAraba.setKutupDegeri(urunKutupDegeri / UrunYorumlariAraba.getYorum().length);
    }

    public String getMotorHacmi() {
        return MotorHacmi;
    }

    public void setMotorHacmi(String motorHacmi) {
        MotorHacmi = motorHacmi;
    }

    public String getYakitCinsi() {
        return YakitCinsi;
    }

    public void setYakitCinsi(String yakitCinsi) {
        YakitCinsi = yakitCinsi;
    }

    public String getAgirlik() {
        return Agirlik;
    }

    public void setAgirlik(String agirlik) {
        Agirlik = agirlik;
    }

    public Tweet getUrunYorumlariAraba() {
        return UrunYorumlariAraba;
    }

    public void setUrunYorumlariAraba(Tweet urunYorumlariAraba) {
        UrunYorumlariAraba = urunYorumlariAraba;
    }
}
