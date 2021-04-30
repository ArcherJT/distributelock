package com.test.code.java.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {


    public static final String QUEUE = "queue";
    public static final String EXCHANGE = "exchange";

    /*默认交换器（direct）实现*/
    @Bean(QUEUE)
    public Queue directQueue(){
        return new Queue("direct",true); //队列名字，是否持久化
    }

    @Bean(EXCHANGE)
    public DirectExchange directExchange(){
        return new DirectExchange("direct",true,false);//交换器名称、是否持久化、是否自动删除
    }

    @Bean
    Binding binding(@Qualifier(QUEUE) Queue queue,
                    @Qualifier(EXCHANGE) DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("direct");
    }

    /*-----------------------------------死信队列-------------------------------------------*/
    //死信队列交换机名
    /*public static final String DEAD_EXCHANGE_NAME = "deadExchange";

    //死信队列队列名 -- A
    public static final String DEAD_QUEUE_A = "deadQueue_A";

    //死信队列队列名 -- B
    public static final String DEAD_QUEUE_B = "deadQueue_B";

    //死信队列队列名
    public static final String DEAD_QUEUE = "deadQueue";

    //死信队列初始化bean名
    public static final String DEAD_EXCHANGE_BEAN = "deadBean";

    //死信队列 map KEY
    public static final String DEAD_EXCHANGE_KEY = "x-dead-letter-exchange";

    //死信队列路由 map key
    public static final String DEAD_ROUTING_KEY = "x-dead-letter-routing-key";

    //路由value
    public static final String DEAD_ROUTING_QUEUE = "deadRoutingQueue";

    //死信队列正常业务测试交换机名
    public static final String BUSINESS_DEAD_EXCHANGE_NAME = "businessExchange";


    //死信队列业务测试初始化bean名
    public static final String BUSINESS_EXCHANGE_BEAN = "businessBean";


    //业务队列队列名 -- A
    public static final String BUSINESS_QUEUE_A = "businessQueue_A";

    //业务队列队列名 -- B
    public static final String BUSINESS_QUEUE_B = "businessQueue_B";

    //业务队列队列名
    public static final String BUSINESS_QUEUE = "businessQueue";

    public static final String DEAD_QUEUE_BEAN = "dead_queue_bean";

    public static final String DEAD_QUEUE_BEAN_A = "dead_queue_bean_a";

    public static final String DEAD_QUEUE_BEAN_B = "dead_queue_bean_b";



    *//*-----------------初始化bean----------------*//*

    //初始化死信对列交换机
    @Bean(DEAD_EXCHANGE_BEAN)
    public DirectExchange deadExchange(){
        return new DirectExchange(DEAD_EXCHANGE_NAME);
    }

    //初始化业务队列交换机
    @Bean(BUSINESS_EXCHANGE_BEAN)
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(BUSINESS_DEAD_EXCHANGE_NAME);
    }

    *//*------------------队列------------------*//*
    //业务队列
    @Bean(BUSINESS_QUEUE_A)
    public Queue businessQueueA(){
        Map<String,Object> map = new HashMap<>(2);
        //绑定死信队列交换机
        map.put(DEAD_EXCHANGE_KEY,DEAD_EXCHANGE_NAME);
        //绑定死信队列路由
        map.put(DEAD_ROUTING_KEY,DEAD_ROUTING_QUEUE);
        return QueueBuilder.durable(BUSINESS_QUEUE).withArguments(map).build();
    }

    @Bean(BUSINESS_QUEUE_B)
    public Queue businessQueueB(){
        Map<String,Object> map = new HashMap<>(2);
        //绑定死信队列交换机
        map.put(DEAD_EXCHANGE_KEY,DEAD_EXCHANGE_NAME);
        //绑定死信队列路由
        map.put(DEAD_ROUTING_KEY,DEAD_ROUTING_QUEUE);
        return QueueBuilder.durable(BUSINESS_QUEUE).withArguments(map).build();
    }

    @Bean(DEAD_QUEUE_BEAN)
    public Queue deadQueue(){
        return new Queue(DEAD_QUEUE,true);
    }

    @Bean(DEAD_QUEUE_BEAN_A)
    public Queue deadQueueA(){
        return new Queue(DEAD_QUEUE_A,true);
    }

    @Bean(DEAD_QUEUE_BEAN_B)
    public Queue deadQueueB(){
        return new Queue(DEAD_QUEUE_B,true);
    }

    //业务队列A绑定关系
    @Bean
    public Binding businessBindingA(@Qualifier(BUSINESS_QUEUE_A) Queue queue ,
                                    @Qualifier(BUSINESS_EXCHANGE_BEAN) FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }


    //业务队列B绑定关系
    @Bean
    public Binding businessBindingB(@Qualifier(BUSINESS_QUEUE_B) Queue queue ,
                                    @Qualifier(BUSINESS_EXCHANGE_BEAN) FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    //死信队列A绑定关系
    @Bean
    public Binding bindingDeadA(@Qualifier(DEAD_QUEUE_BEAN_A) Queue queue,
                                @Qualifier(DEAD_EXCHANGE_BEAN) DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with(DEAD_ROUTING_KEY);
    }

    //死信队列B绑定关系
    @Bean
    public Binding bindingDeadB(@Qualifier(DEAD_QUEUE_BEAN_B) Queue queue,
                                @Qualifier(DEAD_EXCHANGE_BEAN) DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with(DEAD_ROUTING_KEY);
    }*/

    public static final String BUSINESS_EXCHANGE_NAME = "dead.letter.demo.simple.business.exchange";
    public static final String BUSINESS_QUEUEA_NAME = "dead.letter.demo.simple.business.queuea";
    public static final String BUSINESS_QUEUEB_NAME = "dead.letter.demo.simple.business.queueb";
    public static final String DEAD_LETTER_EXCHANGE = "dead.letter.demo.simple.deadletter.exchange";
    public static final String DEAD_LETTER_QUEUEA_ROUTING_KEY = "dead.letter.demo.simple.deadletter.queuea.routingkey";
    public static final String DEAD_LETTER_QUEUEB_ROUTING_KEY = "dead.letter.demo.simple.deadletter.queueb.routingkey";
    public static final String DEAD_LETTER_QUEUEA_NAME = "dead.letter.demo.simple.deadletter.queuea";
    public static final String DEAD_LETTER_QUEUEB_NAME = "dead.letter.demo.simple.deadletter.queueb";

    // 声明业务Exchange
    @Bean("businessExchange")
    public FanoutExchange businessExchange(){
        return new FanoutExchange(BUSINESS_EXCHANGE_NAME);
    }

    // 声明死信Exchange
    @Bean("deadLetterExchange")
    public DirectExchange deadLetterExchange(){
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    // 声明业务队列A
    @Bean("businessQueueA")
    public Queue businessQueueA(){
        Map<String, Object> args = new HashMap<>(4);
//       x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
//       x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEA_ROUTING_KEY);
        args.put("x-message-ttl",5000);
        args.put("x-expires",5000);
        return QueueBuilder.durable(BUSINESS_QUEUEA_NAME).withArguments(args).build();
    }

    // 声明业务队列B
    @Bean("businessQueueB")
    public Queue businessQueueB(){
        Map<String, Object> args = new HashMap<>(4);
//       x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
//       x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEB_ROUTING_KEY);
        args.put("x-message-ttl",5000);
        args.put("x-expires",5000);
        return QueueBuilder.durable(BUSINESS_QUEUEB_NAME).withArguments(args).build();
    }

    // 声明死信队列A
    @Bean("deadLetterQueueA")
    public Queue deadLetterQueueA(){
        return new Queue(DEAD_LETTER_QUEUEA_NAME);
    }

    // 声明死信队列B
    @Bean("deadLetterQueueB")
    public Queue deadLetterQueueB(){
        return new Queue(DEAD_LETTER_QUEUEB_NAME);
    }

    // 声明业务队列A绑定关系
    @Bean
    public Binding businessBindingA(@Qualifier("businessQueueA") Queue queue,
                                    @Qualifier("businessExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    // 声明业务队列B绑定关系
    @Bean
    public Binding businessBindingB(@Qualifier("businessQueueB") Queue queue,
                                    @Qualifier("businessExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    // 声明死信队列A绑定关系
    @Bean
    public Binding deadLetterBindingA(@Qualifier("deadLetterQueueA") Queue queue,
                                      @Qualifier("deadLetterExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEA_ROUTING_KEY);
    }

    // 声明死信队列B绑定关系
    @Bean
    public Binding deadLetterBindingB(@Qualifier("deadLetterQueueB") Queue queue,
                                      @Qualifier("deadLetterExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEB_ROUTING_KEY);
    }
}
