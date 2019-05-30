package com.company;

import redis.clients.jedis.Jedis;

public class RedisSingleton {

    //Redis.io açma işlemini yapan Singleton Design Pattern yapısı..
    private static RedisSingleton instance = null;

    public RedisSingleton() {
        // Exists only to defeat instantiation.
    }

    public static RedisSingleton getInstance() {
        if(instance == null) {
            instance = new RedisSingleton();
        }
        return instance;
    }

    public Jedis RedisOkumaNesnesiniGetir (){
        return new Jedis();
    }

}
