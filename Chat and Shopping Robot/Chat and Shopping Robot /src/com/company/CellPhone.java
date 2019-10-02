package com.company;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;


public class CellPhone extends Product {
	private int cameraSize;
	private String ramSize;
	private Tweet UrunYorumlariCellPhone;

	public CellPhone(int pid,int GarantiSuresi, String brand, String model, int cameraSize, String ramSize) {
		super(pid,GarantiSuresi,brand, model);
		MongoDB verilerDB = new MongoDB();
		String[] ilgiliDegerler = new String[8];


		//İlgili veri MongoDB veritabanında varsa oradan çekmektedir..
		if(verilerDB.CepTelefonuBilgileriniAl(pid,ilgiliDegerler)){

			super.setpId(Integer.parseInt(ilgiliDegerler[0]));
			super.set_GarantiSuresi(Integer.parseInt(ilgiliDegerler[1]));
			super.setpBrand(ilgiliDegerler[2]);
			super.setpModel(ilgiliDegerler[3]);
			this.cameraSize = Integer.parseInt(ilgiliDegerler[4]);
			this.ramSize = ilgiliDegerler[5];

			verilerDB.VeritabaniniKapat();

		}
		else {//mongoDB veritabanında yoksa sıfırdan yeni ürün oluşturup eklemektedir.
			this.cameraSize = cameraSize;
			this.ramSize = ramSize;

			verilerDB.cepTelefonuEkle(pid,GarantiSuresi,brand,model,cameraSize,ramSize);
			verilerDB.VeritabaniniKapat();
		}

        UrunYorumlariCellPhone = new Tweet();
        //Tweet sınıfından hashtag içine marka adı ve model adını yazar.
        UrunYorumlariCellPhone.setHashtag(super.getIdentityDegeri());

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
			//	System.out.println("Silme işlemi başarısız.. : " + e);
			}

			//Twitter yorumları, kutup degerleri ve kutup degerleri ortalamasi bu constructor'da yapıldı..
			Fonksiyonlarim.TwitterYorumuAta(brand, twitterYorumlari);
			UrunYorumlariCellPhone.setYorum(twitterYorumlari);

			for (int i=0 ;i<twitterYorumlari.length; i++){
			    if(twitterYorumlari[i] != null){
					try {
						jedis.lpush(super.getIdentityDegeri(), twitterYorumlari[i]);
                        jedis.sync();
					}catch (Exception e){
					//	System.out.println("Ekleme işlemi başarısız.. : " + e + "\n");
					}
				}

			}


		}
		//Eğer ürün hakkında databasede yorum varsa, o yorumlari database'den çek.
		else{
            for(int i = 0 ; i<urunlerim.size();i++)
				twitterYorumlari[i] = urunlerim.get(i);

			UrunYorumlariCellPhone.setYorum(twitterYorumlari);

		}

		jedis.close();

		float urunKutupDegeri = 0;

		// İlgili yorumun puan değerini döndürür.

		//Strateji deseni ile oluşturulmuş yapının içinde ilk hesaplama yöntemi kullanıldı..
		ContextStrategyPattern contextStrategyPattern = new ContextStrategyPattern(new StrategyDeseniDosyadanPuanlaIlkYontem());

		for (int i = 0; i < UrunYorumlariCellPhone.getYorum().length; i++)
			if (UrunYorumlariCellPhone.getYorum()[i] != null)
				urunKutupDegeri += contextStrategyPattern.executeStrategy(UrunYorumlariCellPhone.getYorum()[i]);


		UrunYorumlariCellPhone.setKutupDegeri(urunKutupDegeri / UrunYorumlariCellPhone.getYorum().length);



    }

	public int getCameraSize() {
		return cameraSize;
	}

	public void setCameraSize(int cameraSize) {
		this.cameraSize = cameraSize;
	}

	public String getRamSize() {
		return ramSize;
	}

	public void setRamSize(String ramSize) {
		this.ramSize = ramSize;
	}

	@Override
	public String toString() {
		return super.toString() + " --CellPhone [cameraSize=" + cameraSize + ", ramSize=" + ramSize + "]";
	}

    public Tweet getUrunYorumlariCellPhone() {
		return UrunYorumlariCellPhone;
	}

	public void setUrunYorumlariCellPhone(Tweet urunYorumlariCellPhone) {
		UrunYorumlariCellPhone = urunYorumlariCellPhone;
	}
}
