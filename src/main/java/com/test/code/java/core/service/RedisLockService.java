package com.test.code.java.core.service;

public interface RedisLockService {


    public void lock();

    public boolean tryLock();

    public void unlock();

    public void heartBeat(String uuid);

    public void getTicket(int count);
}
