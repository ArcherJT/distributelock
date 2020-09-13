package com.test.code.java.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DistributedLock {

    public final String PARAMKEY ="KEYSUFFIX";

    String key() default "redisKey";

    /**
     * 加锁的前缀key 主要用于区分各个业务的方法数据。
     * @return
     */
    String path() default "";

    /**
     * 等待时间-毫秒（tryLock时使用）
     * @return
     */
    long waitTimeOut() default 100  ;

    /**
     * 只有锁时间-以毫秒为单位
     * @return
     */
    long leaseTimeOut() default -1 ;

    /**
     * 是否跟踪（执行时插入一条数据到数据库，并记录执行状态）
     * 对加锁延迟敏感的操作建议设置为false,因为插入数据库，并更新执行状态可能会增加延迟
     */
    boolean trace() default false;

    /**
     * 分布式锁的key
     */
    String localKey();

    /**
     * 锁释放时间 默认五秒
     *
     * @return
     */
    long leaseTime() default 100;

    /**
     * 时间格式 默认：毫秒
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}
