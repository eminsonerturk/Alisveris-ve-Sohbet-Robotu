package com.company;

import com.mongodb.*;

public class MongoDB {

    private Mongo mongo;
    private DB db;
    private DBCollection urunlerim;

    public MongoDB() {
        mongo = new Mongo("localhost", 27017);
        db = mongo.getDB("Urunler");
        urunlerim = db.getCollection("employees");
    }


    public Mongo getMongo() {
        return mongo;
    }

    public void setMongo(Mongo mongo) {
        this.mongo = mongo;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public DBCollection getUrunlerim() {
        return urunlerim;
    }

    public void setUrunlerim(DBCollection urunlerim) {
        this.urunlerim = urunlerim;
    }

    public void VeritabaniniKapat(){
        mongo.close();
    }

    public void cepTelefonuEkle(int pid,int GarantiSuresi, String brand, String model, int cameraSize, String ramSize){

        DBCursor cursor = null;
        try{
            cursor = urunlerim.find(new BasicDBObject().append("urunAdi", "cepTelefonlari"));
        }catch (Exception e){
            System.out.println(e);
        }

        if(cursor.size() == 0){
            try {
                BasicDBObject document = new BasicDBObject();
                document.put("urunAdi", "cepTelefonlari");

                BasicDBObject UrunAyrintilari = new BasicDBObject();
                UrunAyrintilari.put("pid", pid);
                UrunAyrintilari.put("GarantiSuresi", GarantiSuresi);
                UrunAyrintilari.put("brand", brand);
                UrunAyrintilari.put("model", model);
                UrunAyrintilari.put("cameraSize", cameraSize);
                UrunAyrintilari.put("ramSize", ramSize);

                document.put(String.valueOf(pid), UrunAyrintilari);

                urunlerim.insert(document);
            }catch (Exception e){
                System.out.println("Cep telefonlari urununu MongoDb ' ye eklerken hata oluştu: " + e);
            }
        }else if(cursor.size() > 0){

            try {

                cursor = urunlerim.find(new BasicDBObject().append("urunAdi", "cepTelefonlari"));
                //Eski verileri ram'e atar..
                BasicDBObject eskiVerilerim = (BasicDBObject) cursor.next();
                //Yeni gelen veriyi işler..
                BasicDBObject yeniGelenVerim = new BasicDBObject();
                yeniGelenVerim.put("pid", pid);
                yeniGelenVerim.put("GarantiSuresi", GarantiSuresi);
                yeniGelenVerim.put("brand", brand);
                yeniGelenVerim.put("model", model);
                yeniGelenVerim.put("cameraSize", cameraSize);
                yeniGelenVerim.put("ramSize", ramSize);

                //Yeni gelen veriyi eskilerin içine atama işlemi yapar..
                eskiVerilerim.put(String.valueOf(pid), yeniGelenVerim);

                //Eski verileri siler..
                //urunlerim.remove(eskiVerilerim);
                urunlerim.remove(new BasicDBObject().append("urunAdi", "cepTelefonlari"));

                //Yenilenmiş diziyi veritabanina geri ekler..
                urunlerim.insert(eskiVerilerim);
            }catch (Exception e){
                System.out.println("Yeni cep telefonu eklerken sorun oluştu: " + e);
            }
        }
    }

    public boolean CepTelefonuBilgileriniAl (int pid, String[] ilgiliDegerler ){

        try {
            DBCursor bulunanDeger = urunlerim.find(new BasicDBObject().append("urunAdi", "cepTelefonlari").append(String.valueOf(pid) + ".pid", pid));

            if(bulunanDeger.hasNext()){
                DBObject IlgiliUrunBilgileri = bulunanDeger.next();

                DBObject IlgiliUrunDetaylari = (DBObject) IlgiliUrunBilgileri.get(String.valueOf(pid));

                ilgiliDegerler[0] = IlgiliUrunDetaylari.get("pid").toString();
                ilgiliDegerler[1] = IlgiliUrunDetaylari.get("GarantiSuresi").toString();
                ilgiliDegerler[2] = IlgiliUrunDetaylari.get("brand").toString();
                ilgiliDegerler[3] = IlgiliUrunDetaylari.get("model").toString();
                ilgiliDegerler[4] = IlgiliUrunDetaylari.get("cameraSize").toString();
                ilgiliDegerler[5] = IlgiliUrunDetaylari.get("ramSize").toString();
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }

    }

    public void veritabaniniBosalt(){
        DBCursor cursor1 = urunlerim.find();
        while (cursor1.hasNext()) {
            urunlerim.remove(cursor1.next());
        }
    }

    public void ArabaEkle(int pid,int GarantiSuresi, String brand, String model, String motorHacmi,String yakitCinsi, String Agirlik){

        DBCursor cursor = null;
        try{
            cursor = urunlerim.find(new BasicDBObject().append("urunAdi", "Arabalar"));
        }catch (Exception e){
            System.out.println(e);
        }

        if(cursor.size() == 0){
            try {
                BasicDBObject document = new BasicDBObject();
                document.put("urunAdi", "Arabalar");

                BasicDBObject UrunAyrintilari = new BasicDBObject();
                UrunAyrintilari.put("pid", pid);
                UrunAyrintilari.put("GarantiSuresi", GarantiSuresi);
                UrunAyrintilari.put("brand", brand);
                UrunAyrintilari.put("model", model);
                UrunAyrintilari.put("motorHacmi", motorHacmi);
                UrunAyrintilari.put("yakitCinsi", yakitCinsi);
                UrunAyrintilari.put("Agirlik", Agirlik);

                document.put(String.valueOf(pid), UrunAyrintilari);

                urunlerim.insert(document);
            }catch (Exception e){
                System.out.println("Arabalar urununu MongoDb'ye eklerken hata oluştu: " + e);
            }
        }else if(cursor.size() > 0){

            try {

                cursor = urunlerim.find(new BasicDBObject().append("urunAdi", "Arabalar"));
                //Eski verileri ram'e atar..
                BasicDBObject eskiVerilerim = (BasicDBObject) cursor.next();
                //Yeni gelen veriyi işler..
                BasicDBObject yeniGelenVerim = new BasicDBObject();
                yeniGelenVerim.put("pid", pid);
                yeniGelenVerim.put("GarantiSuresi", GarantiSuresi);
                yeniGelenVerim.put("brand", brand);
                yeniGelenVerim.put("model", model);
                yeniGelenVerim.put("motorHacmi", motorHacmi);
                yeniGelenVerim.put("yakitCinsi", yakitCinsi);
                yeniGelenVerim.put("Agirlik", Agirlik);

                //Yeni gelen veriyi eskilerin içine atama işlemi yapar..
                eskiVerilerim.put(String.valueOf(pid), yeniGelenVerim);

                //Eski verileri siler..
                //urunlerim.remove(eskiVerilerim);
                urunlerim.remove(new BasicDBObject().append("urunAdi", "Arabalar"));

                //Yenilenmiş diziyi veritabanina geri ekler..
                urunlerim.insert(eskiVerilerim);
            }catch (Exception e){
                System.out.println("Yeni araba eklerken sorun oluştu: " + e);
            }
        }
    }

    public boolean ArabaBilgileriniAl (int pid, String[] ilgiliDegerler ){

        try {
            DBCursor bulunanDeger = urunlerim.find(new BasicDBObject().append("urunAdi", "Arabalar").append(String.valueOf(pid) + ".pid", pid));

            if(bulunanDeger.hasNext()){
                DBObject IlgiliUrunBilgileri = bulunanDeger.next();

                DBObject IlgiliUrunDetaylari = (DBObject) IlgiliUrunBilgileri.get(String.valueOf(pid));

                ilgiliDegerler[0] = IlgiliUrunDetaylari.get("pid").toString();
                ilgiliDegerler[1] = IlgiliUrunDetaylari.get("GarantiSuresi").toString();
                ilgiliDegerler[2] = IlgiliUrunDetaylari.get("brand").toString();
                ilgiliDegerler[3] = IlgiliUrunDetaylari.get("model").toString();
                ilgiliDegerler[4] = IlgiliUrunDetaylari.get("motorHacmi").toString();
                ilgiliDegerler[5] = IlgiliUrunDetaylari.get("yakitCinsi").toString();
                ilgiliDegerler[6] = IlgiliUrunDetaylari.get("Agirlik").toString();
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }

    }

    public void BisikletEkle(int pid,int GarantiSuresi, String brand, String model, String bisikletTürü,int MaxVitesSayisi){

        DBCursor cursor = null;
        try{
            cursor = urunlerim.find(new BasicDBObject().append("urunAdi", "Bisikletler"));
        }catch (Exception e){
            System.out.println(e);
        }

        if(cursor.size() == 0){
            try {
                BasicDBObject document = new BasicDBObject();
                document.put("urunAdi", "Bisikletler");

                BasicDBObject UrunAyrintilari = new BasicDBObject();
                UrunAyrintilari.put("pid", pid);
                UrunAyrintilari.put("GarantiSuresi", GarantiSuresi);
                UrunAyrintilari.put("brand", brand);
                UrunAyrintilari.put("model", model);
                UrunAyrintilari.put("bisikletTuru", bisikletTürü);
                UrunAyrintilari.put("MaxVitesSayisi", MaxVitesSayisi);


                document.put(String.valueOf(pid), UrunAyrintilari);

                urunlerim.insert(document);
            }catch (Exception e){
                System.out.println("Bisikletler urununu MongoDb'ye eklerken hata oluştu: " + e);
            }
        }else if(cursor.size() > 0){

            try {

                cursor = urunlerim.find(new BasicDBObject().append("urunAdi", "Bisikletler"));
                //Eski verileri ram'e atar..
                BasicDBObject eskiVerilerim = (BasicDBObject) cursor.next();
                //Yeni gelen veriyi işler..
                BasicDBObject yeniGelenVerim = new BasicDBObject();
                yeniGelenVerim.put("pid", pid);
                yeniGelenVerim.put("GarantiSuresi", GarantiSuresi);
                yeniGelenVerim.put("brand", brand);
                yeniGelenVerim.put("model", model);
                yeniGelenVerim.put("bisikletTuru", bisikletTürü);
                yeniGelenVerim.put("MaxVitesSayisi", MaxVitesSayisi);

                //Yeni gelen veriyi eskilerin içine atama işlemi yapar..
                eskiVerilerim.put(String.valueOf(pid), yeniGelenVerim);

                //Eski verileri siler..
                //urunlerim.remove(eskiVerilerim);
                urunlerim.remove(new BasicDBObject().append("urunAdi", "Bisikletler"));

                //Yenilenmiş diziyi veritabanina geri ekler..
                urunlerim.insert(eskiVerilerim);
            }catch (Exception e){
                System.out.println("Yeni bisiklet eklerken sorun oluştu: " + e);
            }
        }
    }


    public boolean BisikletBilgileriniAl (int pid, String[] ilgiliDegerler ){

        try {
            DBCursor bulunanDeger = urunlerim.find(new BasicDBObject().append("urunAdi", "Bisikletler").append(String.valueOf(pid) + ".pid", pid));

            if(bulunanDeger.hasNext()){
                DBObject IlgiliUrunBilgileri = bulunanDeger.next();

                DBObject IlgiliUrunDetaylari = (DBObject) IlgiliUrunBilgileri.get(String.valueOf(pid));

                ilgiliDegerler[0] = IlgiliUrunDetaylari.get("pid").toString();
                ilgiliDegerler[1] = IlgiliUrunDetaylari.get("GarantiSuresi").toString();
                ilgiliDegerler[2] = IlgiliUrunDetaylari.get("brand").toString();
                ilgiliDegerler[3] = IlgiliUrunDetaylari.get("model").toString();
                ilgiliDegerler[4] = IlgiliUrunDetaylari.get("bisikletTuru").toString();
                ilgiliDegerler[5] = IlgiliUrunDetaylari.get("MaxVitesSayisi").toString();

                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }

    }

    public void BuzdolabiEkle(int pid,int GarantiSuresi, String brand, String model, String enerjiSinifi,String agirlik,String boyutlar,String kapasite){

        DBCursor cursor = null;
        try{
            cursor = urunlerim.find(new BasicDBObject().append("urunAdi", "Buzdolaplari"));
        }catch (Exception e){
            System.out.println(e);
        }

        if(cursor.size() == 0){
            try {
                BasicDBObject document = new BasicDBObject();
                document.put("urunAdi", "Buzdolaplari");

                BasicDBObject UrunAyrintilari = new BasicDBObject();
                UrunAyrintilari.put("pid", pid);
                UrunAyrintilari.put("GarantiSuresi", GarantiSuresi);
                UrunAyrintilari.put("brand", brand);
                UrunAyrintilari.put("model", model);
                UrunAyrintilari.put("enerjiSinifi", enerjiSinifi);
                UrunAyrintilari.put("agirlik", agirlik);
                UrunAyrintilari.put("boyutlar", boyutlar);
                UrunAyrintilari.put("kapasite", kapasite);

                document.put(String.valueOf(pid), UrunAyrintilari);

                urunlerim.insert(document);
            }catch (Exception e){
                System.out.println("Buzdolabi urununu MongoDb'ye eklerken hata oluştu: " + e);
            }
        }else if(cursor.size() > 0){

            try {

                cursor = urunlerim.find(new BasicDBObject().append("urunAdi", "Buzdolaplari"));
                //Eski verileri ram'e atar..
                BasicDBObject eskiVerilerim = (BasicDBObject) cursor.next();
                //Yeni gelen veriyi işler..
                BasicDBObject yeniGelenVerim = new BasicDBObject();
                yeniGelenVerim.put("pid", pid);
                yeniGelenVerim.put("GarantiSuresi", GarantiSuresi);
                yeniGelenVerim.put("brand", brand);
                yeniGelenVerim.put("model", model);
                yeniGelenVerim.put("enerjiSinifi", enerjiSinifi);
                yeniGelenVerim.put("agirlik", agirlik);
                yeniGelenVerim.put("boyutlar", boyutlar);
                yeniGelenVerim.put("kapasite", kapasite);

                //Yeni gelen veriyi eskilerin içine atama işlemi yapar..
                eskiVerilerim.put(String.valueOf(pid), yeniGelenVerim);

                //Eski verileri siler..
                //urunlerim.remove(eskiVerilerim);
                urunlerim.remove(new BasicDBObject().append("urunAdi", "Buzdolaplari"));

                //Yenilenmiş diziyi veritabanina geri ekler..
                urunlerim.insert(eskiVerilerim);
            }catch (Exception e){
                System.out.println("Yeni buzdolabi eklerken sorun oluştu: " + e);
            }
        }
    }

    public boolean BuzdolabiBilgileriniAl (int pid, String[] ilgiliDegerler ){

        try {
            DBCursor bulunanDeger = urunlerim.find(new BasicDBObject().append("urunAdi", "Buzdolaplari").append(String.valueOf(pid) + ".pid", pid));

            if(bulunanDeger.hasNext()){
                DBObject IlgiliUrunBilgileri = bulunanDeger.next();

                DBObject IlgiliUrunDetaylari = (DBObject) IlgiliUrunBilgileri.get(String.valueOf(pid));

                ilgiliDegerler[0] = IlgiliUrunDetaylari.get("pid").toString();
                ilgiliDegerler[1] = IlgiliUrunDetaylari.get("GarantiSuresi").toString();
                ilgiliDegerler[2] = IlgiliUrunDetaylari.get("brand").toString();
                ilgiliDegerler[3] = IlgiliUrunDetaylari.get("model").toString();
                ilgiliDegerler[4] = IlgiliUrunDetaylari.get("enerjiSinifi").toString();
                ilgiliDegerler[5] = IlgiliUrunDetaylari.get("agirlik").toString();
                ilgiliDegerler[6] = IlgiliUrunDetaylari.get("boyutlar").toString();
                ilgiliDegerler[7] = IlgiliUrunDetaylari.get("kapasite").toString();

                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }

    }

    public void MobilyaEkle(int pid,int GarantiSuresi, String brand, String model, String AgacMalzemesi,String Ebatlar,String KullanimYeri){

        DBCursor cursor = null;
        try{
            cursor = urunlerim.find(new BasicDBObject().append("urunAdi", "Mobilyalar"));
        }catch (Exception e){
            System.out.println(e);
        }

        if(cursor.size() == 0){
            try {
                BasicDBObject document = new BasicDBObject();
                document.put("urunAdi", "Mobilyalar");

                BasicDBObject UrunAyrintilari = new BasicDBObject();
                UrunAyrintilari.put("pid", pid);
                UrunAyrintilari.put("GarantiSuresi", GarantiSuresi);
                UrunAyrintilari.put("brand", brand);
                UrunAyrintilari.put("model", model);
                UrunAyrintilari.put("agacMalzemesi", AgacMalzemesi);
                UrunAyrintilari.put("ebatlar", Ebatlar);
                UrunAyrintilari.put("KullanimYeri", KullanimYeri);

                document.put(String.valueOf(pid), UrunAyrintilari);

                urunlerim.insert(document);
            }catch (Exception e){
                System.out.println("Mobilya urununu MongoDb'ye eklerken hata oluştu: " + e);
            }
        }else if(cursor.size() > 0){

            try {

                cursor = urunlerim.find(new BasicDBObject().append("urunAdi", "Mobilyalar"));
                //Eski verileri ram'e atar..
                BasicDBObject eskiVerilerim = (BasicDBObject) cursor.next();
                //Yeni gelen veriyi işler..
                BasicDBObject yeniGelenVerim = new BasicDBObject();
                yeniGelenVerim.put("pid", pid);
                yeniGelenVerim.put("GarantiSuresi", GarantiSuresi);
                yeniGelenVerim.put("brand", brand);
                yeniGelenVerim.put("model", model);
                yeniGelenVerim.put("agacMalzemesi", AgacMalzemesi);
                yeniGelenVerim.put("ebatlar", Ebatlar);
                yeniGelenVerim.put("KullanimYeri", KullanimYeri);

                //Yeni gelen veriyi eskilerin içine atama işlemi yapar..
                eskiVerilerim.put(String.valueOf(pid), yeniGelenVerim);

                //Eski verileri siler..
                //urunlerim.remove(eskiVerilerim);
                urunlerim.remove(new BasicDBObject().append("urunAdi", "Mobilyalar"));

                //Yenilenmiş diziyi veritabanina geri ekler..
                urunlerim.insert(eskiVerilerim);
            }catch (Exception e){
                System.out.println("Yeni mobilya eklerken sorun oluştu: " + e);
            }
        }
    }

    public boolean MobilyaBilgileriniAl (int pid, String[] ilgiliDegerler ){

        try {
            DBCursor bulunanDeger = urunlerim.find(new BasicDBObject().append("urunAdi", "Mobilyalar").append(String.valueOf(pid) + ".pid", pid));

            if(bulunanDeger.hasNext()){
                DBObject IlgiliUrunBilgileri = bulunanDeger.next();

                DBObject IlgiliUrunDetaylari = (DBObject) IlgiliUrunBilgileri.get(String.valueOf(pid));

                ilgiliDegerler[0] = IlgiliUrunDetaylari.get("pid").toString();
                ilgiliDegerler[1] = IlgiliUrunDetaylari.get("GarantiSuresi").toString();
                ilgiliDegerler[2] = IlgiliUrunDetaylari.get("brand").toString();
                ilgiliDegerler[3] = IlgiliUrunDetaylari.get("model").toString();
                ilgiliDegerler[4] = IlgiliUrunDetaylari.get("agacMalzemesi").toString();
                ilgiliDegerler[5] = IlgiliUrunDetaylari.get("ebatlar").toString();
                ilgiliDegerler[6] = IlgiliUrunDetaylari.get("KullanimYeri").toString();

                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }

    }

    public void TelevizyonEkle(int pid,int GarantiSuresi, String brand, String model, String ekranEbati,String cozunurluk,int modelYili,String taramaHizi){

        DBCursor cursor = null;
        try{
            cursor = urunlerim.find(new BasicDBObject().append("urunAdi", "Televizyonlar"));
        }catch (Exception e){
            System.out.println(e);
        }

        if(cursor.size() == 0){
            try {
                BasicDBObject document = new BasicDBObject();
                document.put("urunAdi", "Televizyonlar");

                BasicDBObject UrunAyrintilari = new BasicDBObject();
                UrunAyrintilari.put("pid", pid);
                UrunAyrintilari.put("GarantiSuresi", GarantiSuresi);
                UrunAyrintilari.put("brand", brand);
                UrunAyrintilari.put("model", model);
                UrunAyrintilari.put("ekranEbati", ekranEbati);
                UrunAyrintilari.put("cozunurluk", cozunurluk);
                UrunAyrintilari.put("modelYili", modelYili);
                UrunAyrintilari.put("taramaHizi", taramaHizi);

                document.put(String.valueOf(pid), UrunAyrintilari);

                urunlerim.insert(document);
            }catch (Exception e){
                System.out.println("Televizyon urununu MongoDb'ye eklerken hata oluştu: " + e);
            }
        }else if(cursor.size() > 0){

            try {

                cursor = urunlerim.find(new BasicDBObject().append("urunAdi", "Televizyonlar"));
                //Eski verileri ram'e atar..
                BasicDBObject eskiVerilerim = (BasicDBObject) cursor.next();
                //Yeni gelen veriyi işler..
                BasicDBObject yeniGelenVerim = new BasicDBObject();
                yeniGelenVerim.put("pid", pid);
                yeniGelenVerim.put("GarantiSuresi", GarantiSuresi);
                yeniGelenVerim.put("brand", brand);
                yeniGelenVerim.put("model", model);
                yeniGelenVerim.put("ekranEbati", ekranEbati);
                yeniGelenVerim.put("cozunurluk", cozunurluk);
                yeniGelenVerim.put("modelYili", modelYili);
                yeniGelenVerim.put("taramaHizi", taramaHizi);

                //Yeni gelen veriyi eskilerin içine atama işlemi yapar..
                eskiVerilerim.put(String.valueOf(pid), yeniGelenVerim);

                //Eski verileri siler..
                //urunlerim.remove(eskiVerilerim);
                urunlerim.remove(new BasicDBObject().append("urunAdi", "Televizyonlar"));

                //Yenilenmiş diziyi veritabanina geri ekler..
                urunlerim.insert(eskiVerilerim);
            }catch (Exception e){
                System.out.println("Yeni televizyon eklerken sorun oluştu: " + e);
            }
        }
    }


    public boolean TelevizyonBilgileriniAl (int pid, String[] ilgiliDegerler ){

        try {
            DBCursor bulunanDeger = urunlerim.find(new BasicDBObject().append("urunAdi", "Televizyonlar").append(String.valueOf(pid) + ".pid", pid));

            if(bulunanDeger.hasNext()){
                DBObject IlgiliUrunBilgileri = bulunanDeger.next();

                DBObject IlgiliUrunDetaylari = (DBObject) IlgiliUrunBilgileri.get(String.valueOf(pid));

                ilgiliDegerler[0] = IlgiliUrunDetaylari.get("pid").toString();
                ilgiliDegerler[1] = IlgiliUrunDetaylari.get("GarantiSuresi").toString();
                ilgiliDegerler[2] = IlgiliUrunDetaylari.get("brand").toString();
                ilgiliDegerler[3] = IlgiliUrunDetaylari.get("model").toString();
                ilgiliDegerler[4] = IlgiliUrunDetaylari.get("ekranEbati").toString();
                ilgiliDegerler[5] = IlgiliUrunDetaylari.get("cozunurluk").toString();
                ilgiliDegerler[6] = IlgiliUrunDetaylari.get("modelYili").toString();
                ilgiliDegerler[7] = IlgiliUrunDetaylari.get("taramaHizi").toString();

                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }

    }

}