package com.company;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class Buzdolabi extends Product {

    private String enerjiSinifi, Agirlik, Boyutlar, Kapasite;

    private Tweet UrunYorumlariBuzDolabi;

    public Buzdolabi(int pId, int GarantiSuresi, String pBrand, String pModel, String enerjiSinifi, String agirlik, String boyutlar, String kapasite) {
        super(pId, GarantiSuresi, pBrand, pModel);
        MongoDB verilerDB = new MongoDB();
        String[] ilgiliDegerler = new String[10];

        //İlgili veri MongoDB veritabanında varsa oradan çekmektedir..
        if(verilerDB.BuzdolabiBilgileriniAl(pId,ilgiliDegerler)) {

            super.setpId(Integer.parseInt(ilgiliDegerler[0]));
            super.set_GarantiSuresi(Integer.parseInt(ilgiliDegerler[1]));
            super.setpBrand(ilgiliDegerler[2]);
            super.setpModel(ilgiliDegerler[3]);
            this.enerjiSinifi = ilgiliDegerler[4];
            this.Agirlik = ilgiliDegerler[5];
            this.Boyutlar = ilgiliDegerler[6];
            this.Kapasite = ilgiliDegerler[7];

            verilerDB.VeritabaniniKapat();

        }else {//mongoDB veritabanında yoksa sıfırdan yeni ürün oluşturup eklemektedir.

            this.enerjiSinifi = enerjiSinifi;
            Agirlik = agirlik;
            Boyutlar = boyutlar;
            Kapasite = kapasite;

            verilerDB.BuzdolabiEkle(pId,GarantiSuresi,pBrand,pModel,enerjiSinifi,agirlik,boyutlar,kapasite);
            verilerDB.VeritabaniniKapat();

        }

        UrunYorumlariBuzDolabi = new Tweet();
        //Tweet sınıfından hashtag verisinin içine marka adı ve model adı bilgilerini yazar.
        UrunYorumlariBuzDolabi.setHashtag(super.getIdentityDegeri());

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
              //  System.out.println("Silme işlemi başarısız.. : " + e);
            }

            //Twitter yorumları, kutup degerleri ve kutup degerleri ortalamasi bu constructor'da yapıldı..
            Fonksiyonlarim.TwitterYorumuAta(pBrand, twitterYorumlari);
            UrunYorumlariBuzDolabi.setYorum(twitterYorumlari);

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

            UrunYorumlariBuzDolabi.setYorum(twitterYorumlari);

        }

        jedis.close();

        float urunKutupDegeri = 0;

        // İlgili yorumun puan değerini döndürür.

        //Strateji deseni ile oluşturulmuş yapının içinde ilk hesaplama yöntemi kullanıldı..
        ContextStrategyPattern contextStrategyPattern = new ContextStrategyPattern(new StrategyDeseniDosyadanPuanlaIlkYontem());

        for (int i = 0; i < UrunYorumlariBuzDolabi.getYorum().length; i++)
            if (UrunYorumlariBuzDolabi.getYorum()[i] != null)
                urunKutupDegeri += contextStrategyPattern.executeStrategy(UrunYorumlariBuzDolabi.getYorum()[i]);



        UrunYorumlariBuzDolabi.setKutupDegeri(urunKutupDegeri / UrunYorumlariBuzDolabi.getYorum().length);




    }

    public String getEnerjiSinifi() {
        return enerjiSinifi;
    }

    public void setEnerjiSinifi(String enerjiSinifi) {
        this.enerjiSinifi = enerjiSinifi;
    }

    public String getAgirlik() {
        return Agirlik;
    }

    public void setAgirlik(String agirlik) {
        Agirlik = agirlik;
    }

    public String getBoyutlar() {
        return Boyutlar;
    }

    public void setBoyutlar(String boyutlar) {
        Boyutlar = boyutlar;
    }

    public String getKapasite() {
        return Kapasite;
    }

    public void setKapasite(String kapasite) {
        Kapasite = kapasite;
    }


    public Tweet getUrunYorumlariBuzDolabi() {
        return UrunYorumlariBuzDolabi;
    }

    public void setUrunYorumlariBuzDolabi(Tweet urunYorumlariBuzDolabi) {
        UrunYorumlariBuzDolabi = urunYorumlariBuzDolabi;
    }


}
