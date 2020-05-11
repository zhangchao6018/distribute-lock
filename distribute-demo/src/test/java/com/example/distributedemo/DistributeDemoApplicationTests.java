package com.example.distributedemo;

import com.example.distributedemo.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributeDemoApplicationTests {
    @Autowired
    private OrderService orderService;

    /**
     * 模拟并发超卖现象
     * @throws InterruptedException
     */
    @Test
    public void concurrentOrder() throws InterruptedException {
        //Thread.sleep(60000);
        CountDownLatch cdl = new CountDownLatch(50);
        // 所有线程等待  同时执行
        CyclicBarrier cyclicBarrier = new CyclicBarrier(50);

        ExecutorService es = Executors.newFixedThreadPool(50);
        for (int i =0;i<50;i++){
            es.execute(()->{
                try {
                    cyclicBarrier.await();
                    Integer orderId = orderService.createOrder();
                    System.out.println("订单id："+orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    cdl.countDown();
                }
            });
        }
        //执行完毕再关闭资源
        cdl.await();
        es.shutdown();
    }

}
