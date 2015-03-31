package com.hirebigdata.asyncSpider.test;

import com.hirebigdata.asyncSpider.pojo.UserListener;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2015/3/30.
 */
public class hashTest {
    public static void main(String[] args) {
        ConcurrentHashMap<String, UserListener> map = new ConcurrentHashMap<>();
        map.put("abc",new UserListener(1));
        UserListener l = map.get("abc");
        l.incCount();
        l.incCount();
        System.out.println(map.get("abc").getCount());
    }
}
