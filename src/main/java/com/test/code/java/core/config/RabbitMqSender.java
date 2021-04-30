package com.test.code.java.core.config;

import com.test.code.java.core.common.Common;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.test.code.java.core.config.RabbitMqConfig.BUSINESS_EXCHANGE_NAME;

@Component
public class RabbitMqSender {

    @Autowired
    AmqpTemplate rabbitmqTemplate;

    public void send(String message){
        System.out.println("发送消息："+message);
        rabbitmqTemplate.convertAndSend("direct",message);
    }

    public void sendMsg(String msg){
        rabbitmqTemplate.convertSendAndReceive(BUSINESS_EXCHANGE_NAME, "", msg);
    }
}
