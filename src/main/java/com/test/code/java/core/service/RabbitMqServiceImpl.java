package com.test.code.java.core.service;

import com.test.code.java.core.config.RabbitMqSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqServiceImpl implements RabbitMqService{

    @Autowired
    RabbitMqSender rabbitMqSender;

    @Override
    public void sendDirectMsg(String message) {
        rabbitMqSender.send(message);
    }

    @Override
    public void sendDeadMsg(String message) {
        rabbitMqSender.sendMsg(message);
    }


}
