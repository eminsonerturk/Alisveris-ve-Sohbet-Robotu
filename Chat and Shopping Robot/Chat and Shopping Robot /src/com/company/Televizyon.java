package com.company;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class Televizyon extends Product{

    private String _EkranEbati, Cözünürlük,TaramaHizi;
    private int ModelYili;
    private Tweet UrunYorumlariTelevizyon;

    public Televizyon(int pId, int GarantiSuresi, String pBrand, String pModel, String _EkranEbati, String cözünürlük, int modelYili, String taramaHizi) {
        super(pId, GarantiSuresi, pBrand, pModel);


        MongoDB verilerDB = new MongoDB();
        String[] ilgiliDegerler = new String[10];

        //İlgili veri MongoDB veritabanında varsa oradan çekmektedir..
        if(verilerDB.TelevizyonBilgileriniAl(pId,ilgiliDegerler)) {

            super.setpId(Integer.parseInt(ilgiliDegerler[0]));
            super.set_GarantiSuresi(Integer.parseInt(ilgiliDegerler[1]));
            super.setpBrand(ilgiliDegerler[2]);
            super.setpModel(ilgiliDegerler[3]);
            this._EkranEbati = ilgiliDegerler[4];
            this.Cözünürlük = ilgiliDegerler[5];
            this.ModelYili = Integer.parseInt(ilgiliDegerler[6]);
            this.TaramaHizi = ilgiliDegerler[6];

            verilerDB.VeritabaniniKapat();
        }
        else{ //mongoDB veritabanında yoksa sıfırdan yeni ürün oluşturup eklemektedir.

            this._EkranEbati = _EkranEbati;
            Cözünürlük = cözünürlük;
            ModelYili = modelYili;
            TaramaHizi = taramaHizi;

            verilerDB.TelevizyonEkle(pId,GarantiSuresi,pBrand,pModel,_EkranEbati,cözünürlük,modelYili,taramaHizi);
            verilerDB.VeritabaniniKapat();
        }



        UrunYorumlariTelevizyon = new Tweet();
        UrunYorumlariTelevizyon.setHashtag(super.getIdentityDegeri());

        //Twitter yorumları, kutup degerleri ortalamasi bu constructor'da yapıldı..
        String[] twitterYorumlari = new String[10];


        //Redis DAO suna erişim RedisSingleton patterniyle sağlanmıştır..
        Jedis jedis = RedisSingleton.getInstance().RedisOkumaNesnesiniGetir();


        List<String> urunlerim = new ArrayList<>();

        urunlerim.addAll(jedis.lrange(super.getIdentityDegeri(), 0 ,9));


        //Eğer ilgili string idendity'sinin yani o ürün hakkında bir yorum yoksa yorum oluştur ve redis.io ya ekle..
        if(urunlerim.size() == 0) {

            //O keye ait veriler sıfırlandı ve programın hata vermemesi için try catch işlemi yapıldı.
            try {
                jedis.del(super.getIdentityDegeri());
                jedis.sync();
            }catch (Exception e){
              //  System.out.println("Silme işlemi başarısız.. : " + e);
            }

            //Twitter yorumları, kutup degerleri ve kutup degerleri ortalamasi bu constructor'da yapıldı..
            Fonksiyonlarim.TwitterYorumuAta(pBrand, twitterYorumlari);
            UrunYorumlariTelevizyon.setYorum(twitterYorumlari);

            for (int i=0 ;i<twitterYorumlari.length; i++){
                if(twitterYorumlari[i] != null){
                    try {
                        jedis.lpush(super.getIdentityDegeri(), twitterYorumlari[i]);
                        jedis.sync();
                    }catch (Exception e){
                      //  System.out.println("Ekleme işlemi başarısız.. : " + e + "\n");
                    }
                }

            }


        }
        //Eğer ürün hakkında databasede yorum varsa, o yorumlari database'den çek.
        else{
            for(int i = 0 ; i<urunlerim.size();i++)
                twitterYorumlari[i] = urunlerim.get(i);

            UrunYorumlariTelevizyon.setYorum(twitterYorumlari);

        }

        jedis.close();

        float urunKutupDegeri = 0;

        // İlgili yorumun puan değerini döndürür.

        //Strateji deseni ile oluşturulmuş yapının içinde ikinci hesaplama yöntemi kullanıldı..
        ContextStrategyPattern contextStrategyPattern = new ContextStrategyPattern(new StrategyDeseniDosyadanPuanlaIkinciYontem());

        for (int i = 0; i < UrunYorumlariTelevizyon.getYorum().length; i++)
            if (UrunYorumlariTelevizyon.getYorum()[i] != null)
                urunKutupDegeri += contextStrategyPattern.executeStrategy(UrunYorumlariTelevizyon.getYorum()[i]);


        UrunYorumlariTelevizyon.setKutupDegeri(urunKutupDegeri / UrunYorumlariTelevizyon.getYorum().length);

    }

    public Tweet getUrunYorumlariTelevizyon() {
        return UrunYorumlariTelevizyon;
    }

    public void setUrunYorumlariTelevizyon(Tweet urunYorumlariTelevizyon) {
        UrunYorumlariTelevizyon = urunYorumlariTelevizyon;
    }

    public String get_EkranEbati() {
        return _EkranEbati;
    }

    public void set_EkranEbati(String _EkranEbati) {
        this._EkranEbati = _EkranEbati;
    }

    public String getCözünürlük() {
        return Cözünürlük;
    }

    public void setCözünürlük(String cözünürlük) {
        Cözünürlük = cözünürlük;
    }

    public int getModelYili() {
        return ModelYili;
    }

    public void setModelYili(int modelYili) {
        ModelYili = modelYili;
    }

    public String getTaramaHizi() {
        return TaramaHizi;
    }

    public void setTaramaHizi(String taramaHizi) {
        TaramaHizi = taramaHizi;
    }

}
