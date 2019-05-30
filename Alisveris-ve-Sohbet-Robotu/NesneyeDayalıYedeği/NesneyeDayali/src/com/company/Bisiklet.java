package com.company;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class Bisiklet extends Product {

    private String BisikletTürü;
    private int MaxVitesSayısı;

    private Tweet UrunYorumlariBisiklet;

    public Bisiklet(int pId, int GarantiSuresi, String pBrand, String pModel, String bisikletTürü, int maxVitesSayısı) {
        super(pId, GarantiSuresi, pBrand, pModel);

        MongoDB verilerDB = new MongoDB();
        String[] ilgiliDegerler = new String[8];

        //İlgili veri MongoDB veritabanında varsa oradan çekmektedir..
        if(verilerDB.BisikletBilgileriniAl(pId,ilgiliDegerler)){

            super.setpId(Integer.parseInt(ilgiliDegerler[0]));
            super.set_GarantiSuresi(Integer.parseInt(ilgiliDegerler[1]));
            super.setpBrand(ilgiliDegerler[2]);
            super.setpModel(ilgiliDegerler[3]);
            this.BisikletTürü = ilgiliDegerler[4];
            this.MaxVitesSayısı = Integer.parseInt(ilgiliDegerler[5]);

            verilerDB.VeritabaniniKapat();
        }else{//mongoDB veritabanında yoksa sıfırdan yeni ürün oluşturup eklemektedir.

            BisikletTürü = bisikletTürü;
            MaxVitesSayısı = maxVitesSayısı;

            verilerDB.BisikletEkle(pId,GarantiSuresi,pBrand,pModel,bisikletTürü,maxVitesSayısı);
            verilerDB.VeritabaniniKapat();
        }


        UrunYorumlariBisiklet = new Tweet();

        //Twitter sınıfına hashtag olarak marka adı ve model bilgisini yazar.
        UrunYorumlariBisiklet.setHashtag(super.getIdentityDegeri());

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
            Fonksiyonlarim.TwitterYorumuAta(pBrand, twitterYorumlari);
            UrunYorumlariBisiklet.setYorum(twitterYorumlari);

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

            UrunYorumlariBisiklet.setYorum(twitterYorumlari);

        }

        //Jedis veritabanının kapatılma işlemi yapıldı.
        jedis.close();

        float urunKutupDegeri = 0;

        // İlgili yorumun puan değerini döndürür.

        //Strateji deseni ile oluşturulmuş yapının içinde ikinci hesaplama yöntemi kullanıldı..
        ContextStrategyPattern contextStrategyPattern = new ContextStrategyPattern(new StrategyDeseniDosyadanPuanlaIkinciYontem());

        for (int i = 0; i < UrunYorumlariBisiklet.getYorum().length; i++)
            if (UrunYorumlariBisiklet.getYorum()[i] != null)
                urunKutupDegeri += contextStrategyPattern.executeStrategy(UrunYorumlariBisiklet.getYorum()[i]);



        UrunYorumlariBisiklet.setKutupDegeri(urunKutupDegeri / UrunYorumlariBisiklet.getYorum().length);


    }

    public String getBisikletTürü() {
        return BisikletTürü;
    }

    public void setBisikletTürü(String bisikletTürü) {
        BisikletTürü = bisikletTürü;
    }

    public int getMaxVitesSayısı() {
        return MaxVitesSayısı;
    }

    public void setMaxVitesSayısı(int maxVitesSayısı) {
        MaxVitesSayısı = maxVitesSayısı;
    }

    public Tweet getUrunYorumlariBisiklet() {
        return UrunYorumlariBisiklet;
    }

    public void setUrunYorumlariBisiklet(Tweet urunYorumlariBisiklet) {
        UrunYorumlariBisiklet = urunYorumlariBisiklet;
    }


}
