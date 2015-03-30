package com.hirebigdata.asyncSpider.pojo;

/**
 * Created by Administrator on 2015/3/30.
 */
public class TargetService {
    // 异步调用对象
    public  String test(String name){
            System.out.println(name  + "  test is execute! " );
            return  name;
        }
}
