package com.hirebigdata.asyncSpider.main;

import com.hirebigdata.asyncSpider.pojo.DownResquest;
import com.hirebigdata.asyncSpider.pojo.Resource;
import com.hirebigdata.asyncSpider.pojo.UserListener;
import com.hirebigdata.asyncSpider.pojo.zhihu.ZhihuUser;
import com.hirebigdata.asyncSpider.thread.DealThread;
import com.hirebigdata.asyncSpider.thread.DownThread;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by sc on 2015/3/30.
 */
public class Main {
    static final int Init = 0;
    static final int About = 1;
    static String DBname = "scrapy2";
    String uid = null;
    LinkedBlockingQueue<DownResquest> down_queue = new LinkedBlockingQueue(20);
    LinkedBlockingQueue<Resource> deal_queue = new LinkedBlockingQueue(20);
    ConcurrentHashMap<String, UserListener> userMap = new ConcurrentHashMap<String, UserListener>();

    public void fun() {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        DownThread downThread1 = new DownThread(down_queue, deal_queue, userMap);
        DownThread downThread2 = new DownThread(down_queue, deal_queue, userMap);
        DownThread downThread3 = new DownThread(down_queue, deal_queue, userMap);
        DealThread dealThread1 = new DealThread(down_queue, deal_queue, userMap);
        DealThread dealThread2 = new DealThread(down_queue, deal_queue, userMap);
        pool.submit(downThread1);
        pool.submit(downThread2);
        pool.submit(downThread3);
        pool.submit(dealThread1);
        pool.submit(dealThread2);
        pool.shutdown();
        System.out.println("start");
//        String uid = new Mongo().getUserid(DBname, "zhihu_user_data_ids");
        uid = "f83b544be78dacf68fa7c6f5028a4740";
        addNewUser(uid, userMap, down_queue);

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ZhihuUser zhihuUser = userMap.get(uid).getZhihuUser();
        System.out.println("finish");
        System.out.println(zhihuUser.getGender());


    }

    private void addNewUser(String uid, ConcurrentHashMap<String, UserListener> userMap, LinkedBlockingQueue<DownResquest> down_queue) {
        try {
            System.out.println("提交下载请求："+Init+" ,"+uid);
            down_queue.put(new DownResquest(Init, uid));
            userMap.put(uid, new UserListener(1, uid));

        } catch (InterruptedException e) {
            System.out.println("入队出错init");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Main().fun();
    }
}
