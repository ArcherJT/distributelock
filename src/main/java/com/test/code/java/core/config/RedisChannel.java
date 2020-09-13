package com.test.code.java.core.config;

import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.CountDownLatch;

public class RedisChannel extends JedisPubSub {

    private CountDownLatch countDownLatch;

    public RedisChannel( CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    // 获取到订阅消息之后 通过countDownLatch唤醒阻塞线程
    public void onMessage(String channel,String message){
        if(null != countDownLatch){
            countDownLatch.countDown();
        }
    }
}
