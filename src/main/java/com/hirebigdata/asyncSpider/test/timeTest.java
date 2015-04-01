package com.hirebigdata.asyncSpider.test;

import com.hirebigdata.asyncSpider.pojo.DownResquest;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/4/1.
 */
public class timeTest {
    public static void main(String[] args) {
        LinkedBlockingQueue<DownResquest> down_queue = new LinkedBlockingQueue<>(10);
        try {
            System.out.println("1");
            DownResquest poll = down_queue.poll(30, TimeUnit.SECONDS);
            System.out.println(poll);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
