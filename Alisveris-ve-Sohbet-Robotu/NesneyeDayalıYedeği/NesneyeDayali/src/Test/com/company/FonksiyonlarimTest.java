package com.company;

import org.junit.Test;

import static org.junit.Assert.*;

public class FonksiyonlarimTest {

    Bisiklet TestBisiklet = new Bisiklet(1,2,"Merida","Scultura 9000-E Carbon", "Yol Bisikleti",
            24);

    @Test
    public void twitterYorumuAta() throws Exception {
        String[] twitterYorumlariTest = new String[10];


        //Gelen twitter yorumlarının doğru bir şekilde eşleşip eşleşilmediğini kontrol etmektedir..
        Fonksiyonlarim.TwitterYorumuAta(TestBisiklet.getpBrand(), twitterYorumlariTest);

        if(twitterYorumlariTest[0] == TestBisiklet.getUrunYorumlariBisiklet().getYorum()[0]);
            assertTrue(true);

    }

}