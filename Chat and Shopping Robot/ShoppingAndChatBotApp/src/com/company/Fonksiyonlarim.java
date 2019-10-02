package com.company;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Fonksiyonlarim{


    //Twitterdan alınan yorumları twitterYorumlari string dizisine atmakla görevli fonksiyondur.
    public static int twitterVerileriAl(String aranacakYorum, String[] twitterYorumlari, int bayrak){
        ConfigurationBuilder cf = new ConfigurationBuilder();

        cf.setDebugEnabled(true)
                .setOAuthConsumerKey("RYRSvNAbj93qb7ZI01VyHaEgX")
                .setOAuthConsumerSecret("zSHdSsj6rsVoudVDthnbxkKTyOsraSe44EOvflIx36n0KluAJo")
                .setOAuthAccessToken("923245125140140034-Oq8IKzb3SavAxo6cqpkKpSe0CqWDW10")
                .setOAuthAccessTokenSecret("7NKSWXc8BVv9gjMzEUjjzoh0YstVqBYD4XAVDm1ciNTFa");

        TwitterFactory tf = new TwitterFactory(cf.build());

        twitter4j.Twitter twitter = tf.getInstance();

        // get username , status..

        Query query = new Query(aranacakYorum);
        QueryResult result = null;
        query.setCount(5);
        query.setLang("en");

        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        for (Status status : result.getTweets()) {

            if(bayrak == 10)
                break;

            twitterYorumlari[bayrak] = status.getText();
            bayrak++;


        }

        return bayrak;
    }



    //İlgili tweeter query'sinin oluşturulup çıkan sonucun çekilmesini sağlar.

    public static void TwitterYorumuAta(String markaAdi, String[] twitterYorumu){
        int bayrak = 0;
        bayrak = Fonksiyonlarim.twitterVerileriAl(markaAdi,twitterYorumu,bayrak);
        Fonksiyonlarim.twitterVerileriAl(markaAdi,twitterYorumu, bayrak);

    }


}
