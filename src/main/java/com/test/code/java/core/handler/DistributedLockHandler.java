package com.test.code.java.core.handler;

import com.test.code.java.core.annotation.DistributedLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class DistributedLockHandler {

    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(com.test.code.java.core.annotation.DistributedLock)")
    public void RlockAspect() {
    }

    @Around("RlockAspect()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object = null;
        RLock lock = null;
        boolean status = false;

       /* System.out.println("rlockAspect start ");*/

        try {
            DistributedLock rlockInfo = getRlockInfo(proceedingJoinPoint);
            lock = redissonClient.getLock(getLocalKey(proceedingJoinPoint, rlockInfo));

            if (lock != null) {
                status = lock.tryLock(rlockInfo.leaseTime(), rlockInfo.timeUnit());
                if (status) {
                    object = proceedingJoinPoint.proceed();
                }
            }
        } finally {
            if (lock != null && status == true ) {
                lock.unlock();
            }
        }
        return object;
    }

    public DistributedLock getRlockInfo(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        return methodSignature.getMethod().getAnnotation(DistributedLock.class);
    }

    /**
     * redis lock key 生成逻辑  这里只是简单生成，如需投入生产使用，可考虑复杂化
     *
     * @param proceedingJoinPoint
     * @return
     */
    public String getLocalKey(ProceedingJoinPoint proceedingJoinPoint, DistributedLock rlockInfo) {
        StringBuilder localKey = new StringBuilder("Rlock");
        final Object[] args = proceedingJoinPoint.getArgs();

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();
        localKey.append(rlockInfo.key()).append(methodName);

        return localKey.toString();
    }

}
