package com.company;

import java.io.File;

public class SingletonPatternDosyaOku {

    //Dosya açma işlemini yapan Singleton Design Pattern yapısı..
    private static SingletonPatternDosyaOku instance = null;

    private SingletonPatternDosyaOku() {
        // Exists only to defeat instantiation.
    }

    public static SingletonPatternDosyaOku getInstance() {
        if(instance == null) {
            instance = new SingletonPatternDosyaOku();
        }
        return instance;
    }

   public File dosyaOkumaNesnesiniGetir (){
        return new File("./resources/senticnet4.txt");
   }

}
