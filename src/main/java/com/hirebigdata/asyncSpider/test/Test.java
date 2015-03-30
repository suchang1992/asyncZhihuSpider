package com.hirebigdata.asyncSpider.test;

import com.googlecode.asyn4j.core.callback.AsynCallBack;
import com.googlecode.asyn4j.core.handler.CacheAsynWorkHandler;
import com.googlecode.asyn4j.core.handler.DefaultErrorAsynWorkHandler;
import com.googlecode.asyn4j.core.handler.FileAsynServiceHandler;
import com.googlecode.asyn4j.service.AsynService;
import com.googlecode.asyn4j.service.AsynServiceImpl;
import com.hirebigdata.asyncSpider.pojo.TargetBack;
import com.hirebigdata.asyncSpider.pojo.TargetService;

/**
 * Created by sc on 2015/3/30.
 */
public class Test {

    public static void main(String[] args) {
        //  初始化异步工作服务
        AsynService anycService  =  AsynServiceImpl.getService( 300 ,  3000L ,  100 ,  100 , 1000 );
        // 异步工作缓冲处理器
        anycService.setWorkQueueFullHandler( new  CacheAsynWorkHandler( 100 ));
        // 服务启动和关闭处理器
        anycService.setServiceHandler( new FileAsynServiceHandler());
        // 异步工作执行异常处理器
        anycService.setErrorAsynWorkHandler(new DefaultErrorAsynWorkHandler());
        //  启动服务
        anycService.init();

        AsynCallBack back  =   new TargetBack();
        // 添加加异步工作- TargetService 的 test 方法
        anycService.addWork(TargetService.class,
                "test", new Object[] { "asyn4j" + 1 }, new TargetBack());
    }
}
