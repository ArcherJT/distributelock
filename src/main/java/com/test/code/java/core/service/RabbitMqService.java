package com.test.code.java.core.service;

public interface RabbitMqService {

    void sendDirectMsg(String message);

    void sendDeadMsg(String message);
}
