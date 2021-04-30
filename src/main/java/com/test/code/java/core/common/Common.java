package com.test.code.java.core.common;

public class Common {

    /*-----------------------------------死信队列-------------------------------------------*/
    //死信队列交换机名
    public static final String DEAD_EXCHANGE_NAME = "deadExchange";

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
}
