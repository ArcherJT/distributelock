package com.test.code.java;

import com.test.code.java.core.service.MultiThreadProxyHubApiTest;
import com.test.code.java.core.service.RabbitMqService;
import com.test.code.java.core.service.RedisLockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavaApplicationTests {

    @Autowired
    RedisLockService redisLockService;
    private int count = 100;

    @Autowired
    MultiThreadProxyHubApiTest multiThreadProxyHubApiTest;

    @Autowired
    RabbitMqService rabbitMqService;

    @Test
    void ticketTest() throws InterruptedException {
        /*TicketRunnable ticketRunnable = new TicketRunnable();
        Thread thread1 = new Thread(ticketRunnable,"窗口1");
        Thread thread2 = new Thread(ticketRunnable,"窗口2");
        Thread thread3 = new Thread(ticketRunnable,"窗口3");
        Thread thread4 = new Thread(ticketRunnable,"窗口4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        Thread.currentThread().join();*/
        
        multiThreadProxyHubApiTest.run();
        rabbitMqService.sendDirectMsg("my first rabbitmq message");
    }

    public class TicketRunnable implements Runnable{
        @Override
        public void run() {
           /* while (count>0){
                redisLockService.lock();
                try {
                    if(count>0){
                        System.out.println(Thread.currentThread().getName()+"卖了第"+count-- +"张");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(count == 0){
                        System.out.println(Thread.currentThread().getName()+"       停");
                        break;
                    }
                    redisLockService.unlock();

                }
            }*/

            while (count>0){
                redisLockService.getTicket(count--);
            }

        }
    }

}
