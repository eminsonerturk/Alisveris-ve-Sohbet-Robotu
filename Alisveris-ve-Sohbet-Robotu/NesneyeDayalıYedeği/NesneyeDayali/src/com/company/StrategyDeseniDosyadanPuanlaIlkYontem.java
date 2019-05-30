package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StrategyDeseniDosyadanPuanlaIlkYontem implements StrategyDeseniDosyadanOkuPuanlaInterface {
    @Override
    public float doOperation(String arananKelime) {

        //İlgili yorumun ilgili dosyadan okunup puanlama işlemini yapan işlemlerdir..


        //Singleton desen ile dosya okuma nesnesi oluşturulma işlemi yapılmıştır..
        SingletonPatternDosyaOku DosyaOkumaKalibi = SingletonPatternDosyaOku.getInstance();
        //Açılan sınıftan dosya nesnesi alınma işlemi yapılmıştır..
        File myFile =DosyaOkumaKalibi.dosyaOkumaNesnesiniGetir();



        int bayrak = 0, flag = 0;
        float verilenPuan = 0;

        String[] bolumler = arananKelime.split(" ");

        for (int a = 0 ; a < bolumler.length; a++)
            bolumler[a] = bolumler[a].toLowerCase();


        Arrays.sort(bolumler);

        String next = "";
        Scanner scanner = null;

        try {
            scanner = new Scanner(myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        do {
            // senticnet4.txt dosyasının içindeki verilere göre yorum puanı verme işlemini gerçekleştiren algoritma.

            if(containsDigit(next) || flag == 0) {
                next = scanner.next();
                flag = 1;
            }

            if(containsDigit(bolumler[bayrak]))
                bayrak++;

            if(bolumler.length-1 <= bayrak){
                scanner.close();
                break;
            }
            if(next.compareToIgnoreCase(bolumler[bayrak])==0){
                next = scanner.next();
                verilenPuan += Float.parseFloat(next);
                scanner.next();
            }


            if(next.compareToIgnoreCase(bolumler[bayrak]) < 0) {

                while(next.compareToIgnoreCase(bolumler[bayrak]) >= 0){
                    if(containsDigit(next))
                        scanner.next();
                    next = scanner.next();
                    if(bolumler.length-1 <= bayrak){
                        break;
                    }
                }

            }

            if(next.compareToIgnoreCase(bolumler[bayrak]) > 0){
                bayrak++;

                if(bolumler.length-1 <= bayrak)
                    break;

                while(next.compareToIgnoreCase(bolumler[bayrak]) >= 0){
                    if(bolumler.length-1 <= bayrak){
                        break;
                    }

                    if(bayrak < bolumler.length - 1)
                        bayrak++;
                }

            }

            next = scanner.next();


        }while (scanner.hasNext());

        return verilenPuan;
    }

    //Verilen string ifadenin içinde sayı olup olmadığını döndüren fonksiyondur.
    private boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }
}

