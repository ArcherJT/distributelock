package com.test.code.java.core.service;

import com.google.gson.Gson;
import com.test.code.java.core.dao.TestDao;
import com.test.code.java.core.domain.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    TestDao testDao;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<Test> queryTest() {
        Gson gson = new Gson();
        if (redisTemplate.hasKey("test-user")){
            Object object  = redisTemplate.opsForValue().get("test-user");
            System.out.println("======获取redis test-user缓存 "+object.toString());
            List<Test> list = gson.fromJson(object.toString(),List.class);
            return list ;
        }
        System.out.println("======设置redis test-user缓存 ");
        List<Test> list = testDao.queryTest();
        String json = gson.toJson(list);
        redisTemplate.opsForValue().set("test-user",json,1L, TimeUnit.MINUTES);
        return list;
    }

    @Override
    public String sale() {
        String sas = testDao.queryTestById();
        System.out.println("===== "+sas);
        if("0".equals(sas)){
            return "零";
        }
        Integer cre = Integer.valueOf(sas)-1;
        testDao.updateTest(String.valueOf(cre));
        return "干啥干啥于是 = "+String.valueOf(cre);
    }


}
