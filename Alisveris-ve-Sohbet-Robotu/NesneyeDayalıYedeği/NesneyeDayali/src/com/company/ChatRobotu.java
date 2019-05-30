package com.company;

import com.mongodb.*;
import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class ChatRobotu extends JFrame{

    private static JTextField txtEnter = new JTextField();
    private static JTextArea txtChat = new JTextArea();
    private int bayrak = 0, sayac=0, bayrak7=0, enBegenilenUrun = 0;
    private String [] urunBilgileri = new String [7];


    private ArrayList<CellPhone> cellPhoneList = new ArrayList<CellPhone>();
    private ArrayList<Mobilya> MobilyaList = new ArrayList<Mobilya>();
    private ArrayList<Televizyon> TelevizyonList = new ArrayList<Televizyon>();
    private ArrayList<Buzdolabi> BuzdolabiList = new ArrayList<Buzdolabi>();
    private ArrayList<Araba> ArabaList = new ArrayList<Araba>();
    private ArrayList<Bisiklet> BisikletList = new ArrayList<Bisiklet>();


    public ChatRobotu() {

        ArrayList<String> hataMessageList = new ArrayList<String>(
                Arrays.asList("maalesef anlasilmadi...", "lutfen tekrarlar misin", "???", "dedigini anlayamadim.."));

        ArrayList<String> selamlamaMessageList = new ArrayList<String>(
                Arrays.asList("ooo selammm :)..", "merhabalar :)..", "mrb", "hosgeldiniz.."));

        ArrayList<String> vedalasmaMessageList = new ArrayList<String>(
                Arrays.asList("hoscakalin...", "baska bir zaman gorusmek uzere..", "iyi gunler..", "mutlu haftalar dilerim..",
                        "kendinize iyi bakın, güle güle..", "güle güle, baska bir zaman yine bekleriz..",
                        "Kendinizi özletmeden tekrar gelin.."));

        ArrayList<String> halHatirMessageList = new ArrayList<String>(
                Arrays.asList("Tesekkurler, iyiyim. siz?", "iyidir, senden nbr?", "teşekkürler; iyiyim, siz nasılsınız?"));


        //Mongo veritabanini bosaltma işlemini yapan fonk.
       /* MongoDB mongoVeritabanim = new MongoDB();
        mongoVeritabanim.veritabaniniBosalt();
        mongoVeritabanim.VeritabaniniKapat(); */

        //Başlangıçta atanan redis verilerini siler.
        // RedisVeritabaniniBosalt();

       //MangoDB veritabanından kaydedilmiş değerleri alır..
        MongoDbDegerleriAta();


        //Başlangıç arraylistlerini oluşturan fonksiyonun adı.
        //BaslangicAtamalari();

        //Menü oluşturma işlemleri yapılmıştır..
        MenuOlustur();


        // etkilesim
        txtEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uText = txtEnter.getText();
                txtChat.append("You: " + uText + "\n");
                txtEnter.setText("");

                //Selam hoşgeldin merhaba komutları karşılığında verilecek cevapları random atayan fonksiyon.
                if (uText.contains("selam") || uText.contains("hosgeldin") || uText.contains("merhaba")) {
                    // txtChat.append("AI:" + "ooo selamlar" + "\n");
                    decideRandom(selamlamaMessageList);

                }
                //Güle güle komutu karşılığında verilecek cevapları random atayan fonksiyon.
                else if (uText.contains("gule gule")) {

                    decideRandom(vedalasmaMessageList);

                }
                //nasilsiniz? komutu karşılığında verilecek cevapları random atayan fonksiyon.
                else if ( uText.contains("nasilsiniz?")){

                    decideRandom(halHatirMessageList);

                }
                //begenilen ürünler komutu karşılığında verilecek cevapları random atayan fonksiyon.
                else if (uText.contains("begenilen urunler")){
                    //Tavsiye edilecek ürünler yorumPuani büyükten küçüğe doğru listelenecek şekilde sunulur.
                    UrunleriSiralaEkrani();
                    enBegenilenUrun = 1;
                }
                else if (uText.contains("urun sececegim")) {
                    // urun secimi
                    //Urunlerin bulunduğu ekranın gelmesi.
                    UrunleriListeleEkrani();
                    bayrak= 6;

                }
                // 1 numaralı ürün için bilgileri listeler ya da yeni ürün ekler.
                else if (uText.equals("1") && (bayrak7 == 1 || bayrak == 6)) {
                    //Eğer listede eleman varsa listeler
                    if(bayrak == 6){
                       SecilenleriListele ("Cep Telefonu","Telefonlar:" , 1);
                        bayrak = 0;
                    }
                    //Urun ekleme islemini yapar.
                    else{
                        txtChat.append("AI: Lutfen ilgili cep tefonunun garanti suresini, markasini, modelini, camera pixelini ve " +
                                "ram boyutunu aralarinda birer bosluk birakarak giriniz:\n");
                        bayrak = 1;
                    }

                }
                // 2 numaralı ürün için bilgileri listeler ya da yeni ürün ekler.
                else if (uText.equals("2") && (bayrak7 == 1 || bayrak == 6)) {
                    //Eğer listede eleman varsa listeler
                    if(bayrak == 6){
                        SecilenleriListele ("Televizyon","Televizyonlar:" , 2);
                        bayrak =0;
                    }
                    //Urun ekleme islemini yapar.
                    else{
                        txtChat.append("AI: Lutfen ilgili televizyonun garanti suresini, markasini, modelini, ekran ebatı, " +
                            "cozunurlugunu, model yilini ve tarama hizini aralarinda birer bosluk birakarak giriniz:\n");
                        bayrak = 2;
                        bayrak7=0;
                    }


                    // 3 numaralı ürün için bilgileri listeler ya da yeni ürün ekler.
                }else if (uText.equals("3") && (bayrak7 == 1 || bayrak == 6)) {
                    //Eğer listede eleman varsa listeler
                    if(bayrak == 6) {
                        SecilenleriListele ("Bisiklet","Bisikletler:" , 3);
                        bayrak = 0;

                    }
                    //Urun ekleme islemini yapar.
                    else{
                        txtChat.append("AI: Lutfen ilgili bisikletin garanti suresini, markasini, modelini ve türünü(kullanim yerini) " +
                                "aralarinda birer bosluk birakarak giriniz:\n");
                        bayrak = 3;
                        bayrak7=0;
                    }
                }
                // 4 numaralı ürün için bilgileri listeler ya da yeni ürün ekler.
                else if (uText.equals("4") && (bayrak7 == 1 || bayrak == 6)) {
                    //Eğer listede eleman varsa listeler
                    if(bayrak == 6) {
                        SecilenleriListele ("Mobilya","Mobilyalar:" , 4);
                        bayrak=0;
                    }
                    //Urun ekleme islemini yapar.
                    else{
                        txtChat.append("AI: Lutfen ilgili mobilyanın garanti suresini, markasini, modelini, agac malzemesi, " +
                                "ebatini, kullanım yerini aralarinda birer bosluk birakarak giriniz:\n");
                        bayrak = 4;
                        bayrak7=0;
                    }

                }
                // 5 numaralı ürün için bilgileri listeler ya da yeni ürün ekler.
                else if (uText.equals("5") && (bayrak7 == 1 || bayrak == 6)) {
                    //Eğer listede eleman varsa listeler
                    if(bayrak == 6){
                        SecilenleriListele ("Buzdolabi","Buzdolaplari:" , 5);
                        bayrak = 0;
                    }
                    //Urun ekleme islemini yapar.
                    else{
                        txtChat.append("AI: Lutfen ilgili buzdolabinin garanti suresini, markasini, modelini, enerji sinifini, " +
                                "agirligini, boyutlarini ve kapasitesini aralarinda birer bosluk birakarak giriniz:\n");
                        bayrak = 5;
                        bayrak7=0;
                    }
                }
                // 6 numaralı ürün için bilgileri listeler ya da yeni ürün ekler.
                else if (uText.equals("6") && (bayrak7 == 1 || bayrak == 6)) {
                    if(bayrak == 6){
                        SecilenleriListele ("Araba","Arabalar:" , 6);
                        bayrak = 0;
                    }
                    //Urun ekleme islemini yapar.
                    else{
                        txtChat.append("AI: Lutfen ilgili arabanin garanti suresini, markasini, modelini, motor hacmini, " +
                                "yakit cinsi ve agirligini aralarinda birer bosluk birakarak giriniz:\n");
                        bayrak = 5;
                        bayrak7=0;
                    }
                }

                //Urun girme islemini yapan fonksiyonu cagirir.
                else if(uText.contains("urun girecegim")){
                    //Hangi urunun girileceginin bilgisini almak üzere ürünleri listeler.
                    UrunleriListeleEkrani();
                    bayrak7 = 1;

                }else if(bayrak == 1){
                    //Girilen ürünleri ayrıştırarak listeye ekleme işlemini yapan fonksiyon.

                    try{
                        sayac = cellPhoneList.size()+1;
                        urunBilgileri = uText.split(" ");
                        cellPhoneList.add(new CellPhone(sayac, Integer.parseInt(urunBilgileri[0]) ,urunBilgileri[1], urunBilgileri[2], Integer.parseInt(urunBilgileri[3]),
                                urunBilgileri[4]));
                        txtChat.append("AI: Urun basariyla eklendi..\n\n");
                        bayrak = 0;
                    }catch (Exception error){
                        System.out.println("Urun eklenemedi.. : " + error);
                        txtChat.append("AI: Urun basariyla eklenemedi..\n Lutfen girdiginiz degerleri kontrol edip tekrar deneyin.. \n");
                        bayrak=1;
                    }

                    //Tavsiye edilen ürünü tekrar gösterir.
                    if(enBegenilenUrun == 1 && bayrak!=1){
                        UrunleriSiralaEkrani();
                        enBegenilenUrun=0;
                    }

                }else if(bayrak == 2){
                    //Girilen ürünleri ayrıştırarak listeye ekleme işlemini yapan fonksiyon.

                    try {
                        sayac = TelevizyonList.size() +1;
                        urunBilgileri = uText.split(" ");
                        TelevizyonList.add(new Televizyon(sayac, Integer.parseInt(urunBilgileri[0]), urunBilgileri[1], urunBilgileri[2], urunBilgileri[3],
                                urunBilgileri[4], Integer.parseInt(urunBilgileri[5]), urunBilgileri[6]));
                        txtChat.append("AI: Urun basariyla eklendi..\n\n");
                        bayrak = 0;
                    }catch (Exception error){
                        System.out.println("Urun eklenemedi.. : " + error);
                        txtChat.append("AI: Urun basariyla eklenemedi..\n Lutfen girdiginiz degerleri kontrol edip tekrar deneyin.. \n");
                        bayrak=2;
                    }

                    //Tavsiye edilen ürünü tekrar gösterir.
                    if(enBegenilenUrun == 1 && bayrak != 2){
                        UrunleriSiralaEkrani();
                        enBegenilenUrun=0;
                    }
                }
                else if(bayrak == 3) {
                    //Girilen ürünleri ayrıştırarak listeye ekleme işlemini yapan fonksiyon.

                    try{
                       sayac = BisikletList.size()+1;
                        urunBilgileri = uText.split(" ");
                        BisikletList.add(new Bisiklet(sayac, Integer.parseInt(urunBilgileri[0]), urunBilgileri[1], urunBilgileri[2], urunBilgileri[3],
                                Integer.parseInt(urunBilgileri[4])));
                        txtChat.append("AI: Urun basariyla eklendi..\n\n");
                        bayrak = 0;
                    }catch (Exception error){
                            System.out.println("Urun eklenemedi.. : " + error);
                            txtChat.append("AI: Urun basariyla eklenemedi..\n Lutfen girdiginiz degerleri kontrol edip tekrar deneyin.. \n");
                            bayrak=3;
                    }

                    //Tavsiye edilen ürünü tekrar gösterir.
                    if(enBegenilenUrun == 1 && bayrak != 3){
                        UrunleriSiralaEkrani();
                        enBegenilenUrun=0;
                    }
                }
                else if(bayrak == 4) {
                    //Girilen ürünleri ayrıştırarak listeye ekleme işlemini yapan fonksiyon.
                   try{
                        sayac = MobilyaList.size()+1;
                        urunBilgileri = uText.split(" ");
                        MobilyaList.add(new Mobilya(sayac, Integer.parseInt(urunBilgileri[0]), urunBilgileri[1], urunBilgileri[2], urunBilgileri[3],
                                urunBilgileri[4], urunBilgileri[5]));
                        txtChat.append("AI: Urun basariyla eklendi..\n\n");
                        bayrak = 0;
                   }catch (Exception error){
                       System.out.println("Urun eklenemedi.. : " + error);
                       txtChat.append("AI: Urun basariyla eklenemedi..\n Lutfen girdiginiz degerleri kontrol edip tekrar deneyin.. \n");
                       bayrak=4;
                   }

                    //Tavsiye edilen ürünü tekrar gösterir.
                    if(enBegenilenUrun == 1 && bayrak!=4){
                        UrunleriSiralaEkrani();
                        enBegenilenUrun=0;
                    }
                }
                else if(bayrak == 5){

                    //Girilen ürünleri ayrıştırarak listeye ekleme işlemini yapan fonksiyon.
                    try {
                        sayac = BuzdolabiList.size()+1;
                        urunBilgileri = uText.split(" ");
                        BuzdolabiList.add(new Buzdolabi(sayac, Integer.parseInt(urunBilgileri[0]), urunBilgileri[1], urunBilgileri[2], urunBilgileri[3],
                                urunBilgileri[4], urunBilgileri[5], urunBilgileri[6]));
                        txtChat.append("AI: Urun basariyla eklendi..\n\n");
                        bayrak = 0;
                    }catch (Exception error){
                        System.out.println("Urun eklenemedi.. : " + error);
                        txtChat.append("AI: Urun basariyla eklenemedi..\n Lutfen girdiginiz degerleri kontrol edip tekrar deneyin.. \n");
                        bayrak=5;
                    }

                    //Tavsiye edilen ürünü tekrar gösterir.
                    if(enBegenilenUrun == 1 && bayrak!=5){
                        UrunleriSiralaEkrani();
                        enBegenilenUrun=0;
                    }
                }
                else if(bayrak == 6){

                    //Girilen ürünleri ayrıştırarak listeye ekleme işlemini yapan fonksiyon.
                    try {
                        sayac = ArabaList.size()+1;
                        urunBilgileri = uText.split(" ");
                        ArabaList.add(new Araba(sayac, Integer.parseInt(urunBilgileri[0]), urunBilgileri[1], urunBilgileri[2], urunBilgileri[3],
                                urunBilgileri[4], urunBilgileri[5]));
                        txtChat.append("AI: Urun basariyla eklendi..\n\n");
                        bayrak = 0;
                    }catch (Exception error){
                        System.out.println("Urun eklenemedi.. : " + error);
                        txtChat.append("AI: Urun basariyla eklenemedi..\n Lutfen girdiginiz degerleri kontrol edip tekrar deneyin.. \n");
                        bayrak=6;
                    }

                    //Tavsiye edilen ürünü tekrar gösterir.
                    if(enBegenilenUrun == 1 && bayrak!=6){
                        UrunleriSiralaEkrani();
                        enBegenilenUrun=0;
                    }
                }
                //Beklenmedik bir komutla karşılaşırsa hataMessageList'ten rasgele değer alarak cevap olarak döndürür.
                else{
                    decideRandom(hataMessageList);
                }
            }
        });



    }

    //Arraylist içinden rastgele metin alınmasını sağlayan fonksiyon
    private void decideRandom(ArrayList<String> messageList) {
        int decider = (int) (Math.random() * messageList.size());
        txtChat.append("AI: " + messageList.get(decider) + "\n");
    }


    // Menu oluşturma işlemleri yapılmıştır..

    private void MenuOlustur(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        this.setResizable(false);
        this.setLayout(null);
        this.setTitle("Java Bot Example");

        txtEnter.setLocation(5, 540);
        txtEnter.setSize(590, 30);
        txtEnter.requestFocusInWindow();


        txtChat.setLocation(20, 5);
        txtChat.setSize(560, 510);
        txtChat.setEditable(false);
        txtChat.setLineWrap(true);
        txtChat.setWrapStyleWord(true);

        this.add(txtEnter);

        txtChat.setLocation(20, 5);
        txtChat.setSize(560, 510);
        txtChat.setEditable(false);
        txtChat.setLineWrap(true);
        txtChat.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane ( txtChat );
        scroll.setLocation(20, 5);
        scroll.setSize(560,510);
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        scroll.setAutoscrolls(true);

        this.add(scroll);

        this.setVisible(true);

    }


    // Başlangıçta hazır ürün eklemek için kullanılıyor.
    // Eğer hazır atama yapılmasını istiyorsak bu fonksiyonu kullanırız..

    private void BaslangicAtamalari(){
        // urun olusturma
        CellPhone cellPhone = new CellPhone(1, 2, "Asus", "Zenfone 3", 16, "3GB");

        cellPhoneList.add(cellPhone);
        cellPhoneList.add(new CellPhone(2, 2 ,"Apple", "Iphone 7", 64, "2GB"));

        Mobilya mobilya = new Mobilya(1, 5,"Casa", "Albarella modular sofa", "Gürgen", "240x100x82",
                "Living Room");
        MobilyaList.add(mobilya);
        MobilyaList.add(new Mobilya(2,5,"Roche Bobois","Pulsation Large 3-Seat Sofa", "Masif Ahşap", "240x65x112",
                "Living Room"));

        TelevizyonList.add(new Televizyon(1,2,"LG","32MB17HM", "32 inc / 81 cm", "1366 x 768", 2015,
                "100Hz"));
        TelevizyonList.add(new Televizyon(2,2, "Samsung","UE55MU7500", "55 inç / 139 cm", "3840 x 2160",
                2016, "600Hz"));

        BuzdolabiList.add(new Buzdolabi(1,2,"Bosch","Serie | 8", "A+", "96kg","185 x 70 x 77 cm", "459 Litre"));
        BuzdolabiList.add(new Buzdolabi(2,2,"Siemens","iQ500 noFrost", "A+","148kg", "177 x 91 x 92 cm",
                "626 Litre"));

        ArabaList.add(new Araba(1,5, "Volkswagen", "Golf", "1598 cm3", "Benzin",
                "1100 Kg"));
        ArabaList.add(new Araba(2,5, "BMW", "X3", "12998 cm3", "Benzin",
                "1810 Kg"));

        BisikletList.add(new Bisiklet(1,2,"Merida","Scultura 9000-E Carbon", "Yol Bisikleti",
                24));
        BisikletList.add(new Bisiklet(2,2,"Sedona","Code 9 Custom GX1", "Dağ Bisikleti",
                11));

    }



    //Seçilen Urunlerin listelenmesi için kullanılan fonksiyon..

    private void SecilenleriListele (String secilenUrun, String ListelenenUrun, int SecilenNo){

        txtChat.append("AI: " + secilenUrun+ " " + "secildi..." + "\n");
        txtChat.append("AI: " + "Listedeki" + ListelenenUrun + "\n\n");

        if(SecilenNo == 1){
            for (CellPhone cellP : cellPhoneList) {
                txtChat.append("\nAI: " +  cellP.getpBrand() + " " + cellP.getpModel() + "\n\n");

                txtChat.append("AI: Yorumlarin olum puani ortalamasi: " + cellP.getUrunYorumlariCellPhone().getKutupDegeri() + "\n\n");
                txtChat.append("AI: " + "Urun Hakkindaki Yorumlar: " + "\n");


                //İlgili ürünün yorumlarının listelenme işlemini yapar.
                for(int i = 0; i<cellP.getUrunYorumlariCellPhone().getYorum().length; i++){

                    if(cellP.getUrunYorumlariCellPhone().getYorum()[0] == null) {
                        txtChat.append("AI: Urun hakkinda bir yorum bulunamadi..\n");
                        break;
                    }else if(cellP.getUrunYorumlariCellPhone().getYorum()[i] == null){
                        break;
                    }

                    txtChat.append("AI: " + cellP.getUrunYorumlariCellPhone().getYorum()[i] + "\n");


                }

            }
        }
        if (SecilenNo == 2){
            for (Televizyon televizyon : TelevizyonList) {
                txtChat.append("\nAI: "  + televizyon.getpBrand() + " " + televizyon.getpModel() + "\n\n");

                txtChat.append("AI: Yorumlarin olum puani ortalamasi: " + televizyon.getUrunYorumlariTelevizyon().getKutupDegeri() + "\n\n");
                txtChat.append("AI: " + "Urun Hakkindaki Yorumlar: " + "\n\n");


                //İlgili ürünün yorumlarının listelenme işlemini yapar.
                for(int i = 0; i<televizyon.getUrunYorumlariTelevizyon().getYorum().length; i++){

                    if(televizyon.getUrunYorumlariTelevizyon().getYorum()[0] == null) {
                        txtChat.append("AI: Urun hakkinda bir yorum bulunamadi..\n");
                        break;
                    }else if(televizyon.getUrunYorumlariTelevizyon().getYorum()[i] == null)
                        break;


                    txtChat.append("AI: " + televizyon.getUrunYorumlariTelevizyon().getYorum()[i] + "\n");

                }


            }
        }
        if (SecilenNo == 3){
            for (Bisiklet bisiklet : BisikletList ) {
                txtChat.append("\nAI: " + bisiklet.getpBrand() + " " + bisiklet.getpModel() + "\n\n");

                txtChat.append("AI: Yorumlarin olum puani ortalamasi: " + bisiklet.getUrunYorumlariBisiklet().getKutupDegeri() + "\n\n");

                txtChat.append("AI: " + "Urun Hakkindaki Yorumlar: " + "\n\n");



                //İlgili ürünün yorumlarının listelenme işlemini yapar.
                for(int i = 0; i<bisiklet.getUrunYorumlariBisiklet().getYorum().length; i++){

                    if(bisiklet.getUrunYorumlariBisiklet().getYorum()[0] == null) {
                        txtChat.append("AI: Urun hakkinda bir yorum bulunamadi...\n");
                        break;
                    }else if(bisiklet.getUrunYorumlariBisiklet().getYorum()[i] == null)
                        break;

                    txtChat.append("AI: " + bisiklet.getUrunYorumlariBisiklet().getYorum()[i] + "\n");

                }

            }
        }
        if (SecilenNo == 4){
            for (Mobilya mobilya : MobilyaList ) {
                txtChat.append("\nAI: " + mobilya.getpBrand() + " " + mobilya.getpModel() + "\n\n");

                txtChat.append("AI: Yorumlarin olum puani ortalamasi: " + mobilya.getUrunYorumlariMobilya().getKutupDegeri() + "\n\n");

                txtChat.append("AI: " + "Urun Hakkindaki Yorumlar: " + "\n\n");



                //İlgili ürünün yorumlarının listelenme işlemini yapar.
                for(int i = 0; i<mobilya.getUrunYorumlariMobilya().getYorum().length; i++){

                    if(mobilya.getUrunYorumlariMobilya().getYorum()[0] == null) {
                        txtChat.append("AI: Urun hakkinda bir yorum bulunamadi....\n");
                        break;
                    }else if(mobilya.getUrunYorumlariMobilya().getYorum()[i] == null)
                        break;

                    txtChat.append("AI: " + mobilya.getUrunYorumlariMobilya().getYorum()[i] + "\n");

                }
            }
        }
        if (SecilenNo == 5){
            for (Buzdolabi buzdolabi : BuzdolabiList ) {
                txtChat.append("\nAI: " + buzdolabi.getpBrand() + " " + buzdolabi.getpModel() + "\n\n");
                txtChat.append("AI: Yorumlarin olum puani ortalamasi: " + buzdolabi.getUrunYorumlariBuzDolabi().getKutupDegeri() + "\n\n");
                txtChat.append("AI: " + "Urun Hakkindaki Yorumlar: " + "\n\n");



                //İlgili ürünün yorumlarının listelenme işlemini yapar.
                for(int i = 0; i<buzdolabi.getUrunYorumlariBuzDolabi().getYorum().length; i++){

                    if(buzdolabi.getUrunYorumlariBuzDolabi().getYorum()[0] == null) {
                        txtChat.append("AI: Urun hakkinda bir yorum bulunamadi.....\n");
                        break;
                    }else if(buzdolabi.getUrunYorumlariBuzDolabi().getYorum()[i] == null)
                        break;

                    txtChat.append("AI: " + buzdolabi.getUrunYorumlariBuzDolabi().getYorum()[i] + "\n");

                }
            }
        }
        if (SecilenNo == 6){
            for (Araba araba: ArabaList ) {
                txtChat.append("\nAI: " + araba.getpBrand() + " " + araba.getpModel() + "\n\n");
                txtChat.append("AI: Yorumlarin olum puani ortalamasi: " + araba.getUrunYorumlariAraba().getKutupDegeri() + "\n\n");
                txtChat.append("AI: " + "Urun Hakkindaki Yorumlar: " + "\n\n");



                //İlgili ürünün yorumlarının listelenme işlemini yapar.
                for(int i = 0; i<araba.getUrunYorumlariAraba().getYorum().length; i++){

                    if(araba.getUrunYorumlariAraba().getYorum()[0] == null) {
                        txtChat.append("AI: Urun hakkinda bir yorum bulunamadi.....\n");
                        break;
                    }else if(araba.getUrunYorumlariAraba().getYorum()[i] == null)
                        break;

                    txtChat.append("AI: " + araba.getUrunYorumlariAraba().getYorum()[i] + "\n");

                }
            }
        }
    }

    // Urunlerin listelenmek istendiği zaman oluşturacağı menu oluşturulmuştur.
    private void UrunleriListeleEkrani(){
        txtChat.append("AI: " + "Lutfen urunu seciniz:" + "\n");
        txtChat.append("AI: " + "1: Cep Telefonu" + "\n");
        txtChat.append("AI: " + "2: Televizyon" + "\n");
        txtChat.append("AI: " + "3: Bisiklet" + "\n");
        txtChat.append("AI: " + "4: Mobilya" + "\n");
        txtChat.append("AI: " + "5: BuzDolabi" + "\n");
        txtChat.append("AI: " + "6: Araba" + "\n");
    }

    // Listedeki ürünlerin yorumPuanlari ortalamasinin büyükten küçüğe yapılıp listelenmesi işlemini yapmaktadır.
    private void UrunleriSiralaEkrani(){
        int flag = 0;
        Tweet[] urunler = new Tweet[MobilyaList.size()+ cellPhoneList.size()+ TelevizyonList.size() + BuzdolabiList.size() + ArabaList.size() + BisikletList.size()];
        for (Mobilya mobilya : MobilyaList ){
            urunler[flag] = mobilya.getUrunYorumlariMobilya();
            flag++;
        }
        for (CellPhone cellP : cellPhoneList){
            urunler[flag] = cellP.getUrunYorumlariCellPhone();
            flag++;
        }
        for (Televizyon televizyon : TelevizyonList) {
            urunler[flag] = televizyon.getUrunYorumlariTelevizyon();
            flag++;
        }
        for (Bisiklet bisiklet : BisikletList ) {
            urunler[flag] = bisiklet.getUrunYorumlariBisiklet();
            flag++;
        }
        for (Buzdolabi buzdolabi : BuzdolabiList ) {
            urunler[flag] = buzdolabi.getUrunYorumlariBuzDolabi();
            flag++;
        }
        for (Araba araba : ArabaList ) {
            urunler[flag] = araba.getUrunYorumlariAraba();
            flag++;
        }

        Arrays.sort(urunler);


        txtChat.append("AI: " + "En begenilen urun:" + urunler[0].getHashtag() + " Yorum Puani: " + urunler[0].getKutupDegeri() + "\n");

        for(int i = 1 ;i< urunler.length ; i++)
            txtChat.append("AI: " + "Diger urun:" + urunler[i].getHashtag() + " Yorum Puani: " + urunler[i].getKutupDegeri() + "\n");



    }


    private boolean RedisVeritabaniniBosalt(){


        Jedis jedis = new Jedis();

        try {
            List<String> keyDegerleri = new ArrayList<String>();
            keyDegerleri.addAll(jedis.keys("*"));

            for (String degerler : keyDegerleri)
                jedis.del(degerler);

            return true;

        }catch (Exception e){
            System.out.println("Verileri silerken hata oluştu: " + e);
            return false;
        }
    }


    //Veritabanından urun bilgilerini alan fonksiyon
    private void MongoDbDegerleriAta() {

        MongoDB MongoVeritabanim = new MongoDB();

        int bayrak_pid = 1;

        String[] tabloIsimleri = {"cepTelefonlari","Arabalar","Bisikletler", "Buzdolaplari", "Mobilyalar", "Televizyonlar"};

        try {
            DBCursor bulunanDeger = MongoVeritabanim.getUrunlerim().find(new BasicDBObject().append("urunAdi", tabloIsimleri[0]));

            DBObject IlgiliUrunBilgileri = bulunanDeger.next();

            DBObject IlgiliUrunDetaylari;

            bayrak_pid = 1;

            while (true) {

                IlgiliUrunDetaylari = (DBObject) IlgiliUrunBilgileri.get(String.valueOf(bayrak_pid));

                if (IlgiliUrunDetaylari == null)
                    break;

                cellPhoneList.add(new CellPhone(Integer.valueOf(IlgiliUrunDetaylari.get("pid").toString()),Integer.valueOf(IlgiliUrunDetaylari.get("GarantiSuresi").toString())  ,
                        IlgiliUrunDetaylari.get("brand").toString(), IlgiliUrunDetaylari.get("model").toString(), Integer.valueOf(IlgiliUrunDetaylari.get("cameraSize").toString()), IlgiliUrunDetaylari.get("ramSize").toString()));

                bayrak_pid++;
            }

            bulunanDeger = MongoVeritabanim.getUrunlerim().find(new BasicDBObject().append("urunAdi", tabloIsimleri[1]));

            IlgiliUrunBilgileri = bulunanDeger.next();

            bayrak_pid = 1;


            while (true) {

                IlgiliUrunDetaylari = (DBObject) IlgiliUrunBilgileri.get(String.valueOf(bayrak_pid));

                if (IlgiliUrunDetaylari == null)
                    break;

                ArabaList.add(new Araba(Integer.valueOf(IlgiliUrunDetaylari.get("pid").toString()),Integer.valueOf(IlgiliUrunDetaylari.get("GarantiSuresi").toString())  ,
                        IlgiliUrunDetaylari.get("brand").toString(), IlgiliUrunDetaylari.get("model").toString(), IlgiliUrunDetaylari.get("motorHacmi").toString(),IlgiliUrunDetaylari.get("yakitCinsi").toString(),
                        IlgiliUrunDetaylari.get("Agirlik").toString()));

                bayrak_pid++;
            }

            bulunanDeger = MongoVeritabanim.getUrunlerim().find(new BasicDBObject().append("urunAdi", tabloIsimleri[2]));

            IlgiliUrunBilgileri = bulunanDeger.next();

            bayrak_pid = 1;



            while (true) {

                IlgiliUrunDetaylari = (DBObject) IlgiliUrunBilgileri.get(String.valueOf(bayrak_pid));

                if (IlgiliUrunDetaylari == null)
                    break;

                BisikletList.add(new Bisiklet(Integer.valueOf(IlgiliUrunDetaylari.get("pid").toString()),Integer.valueOf(IlgiliUrunDetaylari.get("GarantiSuresi").toString())  ,
                        IlgiliUrunDetaylari.get("brand").toString(), IlgiliUrunDetaylari.get("model").toString(), IlgiliUrunDetaylari.get("bisikletTuru").toString(),Integer.parseInt(IlgiliUrunDetaylari.get("MaxVitesSayisi").toString())));

                bayrak_pid++;
            }

            bulunanDeger = MongoVeritabanim.getUrunlerim().find(new BasicDBObject().append("urunAdi", tabloIsimleri[3]));

            IlgiliUrunBilgileri = bulunanDeger.next();

            bayrak_pid = 1;


            while (true) {

                IlgiliUrunDetaylari = (DBObject) IlgiliUrunBilgileri.get(String.valueOf(bayrak_pid));


                if (IlgiliUrunDetaylari == null)
                    break;

                BuzdolabiList.add(new Buzdolabi(Integer.valueOf(IlgiliUrunDetaylari.get("pid").toString()),Integer.valueOf(IlgiliUrunDetaylari.get("GarantiSuresi").toString())  ,
                        IlgiliUrunDetaylari.get("brand").toString(), IlgiliUrunDetaylari.get("model").toString(), IlgiliUrunDetaylari.get("enerjiSinifi").toString(),IlgiliUrunDetaylari.get("agirlik").toString(),
                        IlgiliUrunDetaylari.get("boyutlar").toString(), IlgiliUrunDetaylari.get("kapasite").toString()));

                bayrak_pid++;

            }

            bulunanDeger = MongoVeritabanim.getUrunlerim().find(new BasicDBObject().append("urunAdi", tabloIsimleri[4]));

            IlgiliUrunBilgileri = bulunanDeger.next();

            bayrak_pid = 1;


            while (true) {

                IlgiliUrunDetaylari = (DBObject) IlgiliUrunBilgileri.get(String.valueOf(bayrak_pid));

                if (IlgiliUrunDetaylari == null)
                    break;

                MobilyaList.add(new Mobilya(Integer.valueOf(IlgiliUrunDetaylari.get("pid").toString()),Integer.valueOf(IlgiliUrunDetaylari.get("GarantiSuresi").toString())  ,
                        IlgiliUrunDetaylari.get("brand").toString(), IlgiliUrunDetaylari.get("model").toString(), IlgiliUrunDetaylari.get("agacMalzemesi").toString(),IlgiliUrunDetaylari.get("ebatlar").toString(),
                        IlgiliUrunDetaylari.get("KullanimYeri").toString()));

                bayrak_pid++;

            }

            bulunanDeger = MongoVeritabanim.getUrunlerim().find(new BasicDBObject().append("urunAdi", tabloIsimleri[5]));

            IlgiliUrunBilgileri = bulunanDeger.next();

            bayrak_pid = 1;


            while (true) {

                IlgiliUrunDetaylari = (DBObject) IlgiliUrunBilgileri.get(String.valueOf(bayrak_pid));

                if (IlgiliUrunDetaylari == null)
                    break;

                TelevizyonList.add(new Televizyon(Integer.valueOf(IlgiliUrunDetaylari.get("pid").toString()),Integer.valueOf(IlgiliUrunDetaylari.get("GarantiSuresi").toString())  ,
                        IlgiliUrunDetaylari.get("brand").toString(), IlgiliUrunDetaylari.get("model").toString(), IlgiliUrunDetaylari.get("ekranEbati").toString(),IlgiliUrunDetaylari.get("cozunurluk").toString(),
                        Integer.valueOf(IlgiliUrunDetaylari.get("modelYili").toString()),IlgiliUrunDetaylari.get("taramaHizi").toString()));

                bayrak_pid++;

            }

            MongoVeritabanim.VeritabaniniKapat();

        } catch (Exception e) {
        }

    }
}


