package com.company;


//İstenen tweet class'ı ve Comparable arayüzünün kullanımı yapılmıştır.

public class Tweet implements Comparable<Tweet> {

    private String hashtag;
    private String[] yorum;
    private float kutupDegeri;


    public Tweet() {
    }

    public Tweet(String hashtag, float kutupDegeri) {
        this.hashtag = hashtag;
        this.yorum = new String[10];
        this.kutupDegeri = kutupDegeri;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String[] getYorum() {
        return yorum;
    }

    public void setYorum(String[] yorum) {
        this.yorum = yorum;
    }

    public float getKutupDegeri() {
        return kutupDegeri;
    }

    public void setKutupDegeri(float kutupDegeri) {
        this.kutupDegeri = kutupDegeri;
    }


    //Dizideki elemanları sıralamak için kullanılan arayüz fonksiyonu..
    @Override
    public int compareTo(Tweet o) {
        return Float.compare(o.getKutupDegeri(),this.getKutupDegeri());
    }
}
