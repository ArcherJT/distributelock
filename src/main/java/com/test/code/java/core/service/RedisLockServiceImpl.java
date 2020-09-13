package com.test.code.java.core.service;

import com.test.code.java.core.annotation.DistributedLock;
import com.test.code.java.core.config.RedisChannel;
import com.test.code.java.core.dao.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Service
public class RedisLockServiceImpl implements RedisLockService {

    private static final String LOCK = "redis-lock";

    private static final String CHANNEL = "redis-publish";

    private static final ThreadLocal threadLocal = new ThreadLocal();

    // 定时任务执行器
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);

    // 存放加锁时创建的定时任务
    private static ConcurrentHashMap<String,Future> concurrentHashMap = new ConcurrentHashMap<String, Future>();

/*    @Autowired
    JedisPool jedisPool;*/
    @Autowired
    JedisConnectionFactory jedisConnectionFactory;
    @Autowired
    TestDao testDao;


    @Override
    public void lock() {
        while (true){
            if(tryLock()){
                return;
            }
            CountDownLatch countDownLatch = new CountDownLatch(3);
            RedisChannel redisChannel = new RedisChannel(countDownLatch);
            //jedis subscribe 是阻塞的，因此创建新线程执行,监听channel
            Jedis jedis = (Jedis) jedisConnectionFactory.getConnection().getNativeConnection();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jedis.subscribe(redisChannel,CHANNEL);
                }
            }).start();
            // 阻塞等待解锁
            try {
                countDownLatch.await(20,TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch = null;
            // 加锁前取消订阅
            redisChannel.unsubscribe();
            jedis.close();
            /*try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }

    }

/*    @Override
    public void lockInterruptibly() throws InterruptedException {

    }*/

    @Override
    public boolean tryLock() {
        String uuid = UUID.randomUUID().toString();
       // System.out.println("threadLocal.get() = "+threadLocal.get());
        if(null == threadLocal.get()){
            threadLocal.set(uuid);
        }
        /*Jedis jedis = jedisPool.getResource();*/
        try (Jedis jedis = (Jedis) jedisConnectionFactory.getConnection().getNativeConnection()){
            /*(LOCK,uuid,"NX","PX",3000)*/
            SetParams params = SetParams.setParams().nx().px(10);
            String set = jedis.set(LOCK,threadLocal.get().toString(),params);
            if("OK".equals(set)){
               // System.out.println("加锁成功 = "+uuid);

               /* threadLocal.set(uuid);*/
                heartBeat(uuid);
                return true;
            }
           /* Thread.sleep(10);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
       // System.out.println("加锁失败 = "+uuid);
        return false;
    }

/*    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }*/

    @Override
    public void unlock() {
        try(Jedis jedis = (Jedis) jedisConnectionFactory.getConnection().getNativeConnection()) {
            String lua = "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                    "   return redis.call('del',KEYS[1]) " +
                    "else" +
                    "   return 0 " +
                    "end";
            Object object = jedis.eval(lua, Collections.singletonList(LOCK), Collections.<String>singletonList(threadLocal.get().toString()));
           // System.out.println("解锁 = "+threadLocal.get()+"          object=="+object.toString());
       /* if("1".equals(object.toString())){
            System.out.println("解锁 = "+threadLocal.get());
            return true;
        }*/
            // 解锁时停止心跳检测
            Future future = concurrentHashMap.get(threadLocal.get());
          //  System.out.println("时停止心跳检测 = "+threadLocal.get());
            //发布消息
            jedis.publish(CHANNEL,"unlock");
          //  System.out.println("发布消息 = "+threadLocal.get());
            jedis.close();
        } catch (Exception e) {
           System.out.println("unlock 异常 ="+e);
        }
    }
/*
    @Override
    public Condition newCondition() {
        return null;
    }*/

    @Override
    public void heartBeat(String uuid) {
        if(concurrentHashMap.contains(uuid)){
           // System.out.println("心跳还在 = "+uuid);
            return;
        }
     //   System.out.println("锁存在 延长时间 = "+uuid);
        Future future = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Jedis jedis = null;
                try {
                    jedis = (Jedis) jedisConnectionFactory.getConnection().getNativeConnection();
               //     System.out.println("锁存在，但是快到失效时间，延长时间 = "+uuid+"jedis.get(LOCK)= "+jedis.get(LOCK));
                    // 锁存在，但是快到失效时间，延长时间
                    if(uuid.equals(jedis.get(LOCK))){
                 //       System.out.println("续命 = "+uuid);
                        jedis.expire(uuid,10);
                    }else {
                    //    System.out.println("移除 = "+uuid);
                        concurrentHashMap.get(uuid).cancel(true);
                        concurrentHashMap.remove(uuid);
                    }
                } catch (Exception e) {
                    System.out.println("异常=="+e);
                }finally {
                    jedis.close();
                }
            }
        },1,20,TimeUnit.SECONDS);
       // System.out.println("发布 = "+uuid);
        concurrentHashMap.put(uuid,future);
    }

    @Override
    @DistributedLock(localKey = "ticket-key",waitTimeOut = 10,timeUnit = TimeUnit.SECONDS)
    public void getTicket(int count) {
        System.out.println(Thread.currentThread().getName()+"卖了第"+count +"张");
        String name = "第"+count +"张";
        testDao.insertTest(name,String.valueOf(count),name);
    }
}
