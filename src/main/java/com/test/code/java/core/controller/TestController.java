package com.test.code.java.core.controller;

import com.test.code.java.core.domain.Test;
import com.test.code.java.core.service.RabbitMqService;
import com.test.code.java.core.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @Autowired
    RabbitMqService rabbitMqService;

    @RequestMapping(value = "/query")
    public List<Test> queryTest(){
        return testService.queryTest();
    }

    @RequestMapping(value = "/sale")
    public String sale(){
        return testService.sale();
    }

    @RequestMapping(value = "send")
    public void sendMq(@PathParam("msg") String msg){
     //   rabbitMqService.sendDirectMsg(msg);

        rabbitMqService.sendDeadMsg(msg);
    }
}
