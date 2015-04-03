package com.hirebigdata.asyncSpider.thread;

import com.hirebigdata.asyncSpider.pojo.DownResquest;
import com.hirebigdata.asyncSpider.pojo.Resource;
import com.hirebigdata.asyncSpider.pojo.UserListener;
import com.hirebigdata.asyncSpider.utils.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by sc on 2015/3/30.
 */
public class DownThread implements Callable {
    static final int InitPAGE = 0;
    static final int AboutPAGE = 1;
    static final int TopicPAGE = 2;
    static final int ColumnPAGE = 3;
    static final int FollowerPAGE = 4;
    static final int FolloweePAGE = 5;
    static final int QuestionListPAGE = 6;
    static final int AnswerListPAGE = 7;
    static final int QuestionPAGE = 8;
    static final int AnswerPAGE = 9;

    private static Map<String, String> header = new HashMap<String, String>();
    LinkedBlockingQueue<DownResquest> down_queue;
    LinkedBlockingQueue<Resource> deal_queue;
    ConcurrentHashMap<String,UserListener> userMap;
    DownResquest downResquest;

    public DownThread(LinkedBlockingQueue<DownResquest> down_queue, LinkedBlockingQueue<Resource> deal_queue, ConcurrentHashMap<String, UserListener> userMap) {
        this.down_queue = down_queue;
        this.deal_queue = deal_queue;
        this.userMap = userMap;
    }

    private void getResquest(){
        try {
            downResquest = down_queue.take();
        } catch (InterruptedException e) {
            System.out.println("出队出错");
            e.printStackTrace();
        }
    }

    private void putResource(String html,int flag){
        try {
            deal_queue.put(new Resource(flag,downResquest.getUser_data_id(), html));
        } catch (InterruptedException e) {
            System.out.println("资源入队出错");
            e.printStackTrace();
        }
    }
    //普通页面下载
    private void DownLoader(String url, int num){
        String html = DownLoader(url);
        int i = 0;
        while (html==null && i++ < num) {
            try {
                TimeUnit.SECONDS.sleep(1);
                html = DownLoader(url);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (html != null)
            putResource(html, downResquest.getFlag());
        else
            System.out.println("多次尝试获得网页失败:"+downResquest.getFlag()+",url:"+url);
    }

    private String DownLoader(String url){
        try {
            String html = new HttpUtil().get(url, getHeader());
            if(html.equals("404"))
                return null;
            return html;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Map<String, String> getHeader() {
        header.put("Origin", "http://www.zhihu.com");
        header.put("Host", "www.zhihu.com");
        header.put("Connection", "keep-alive");
        header.put("Accept-Encoding", "gzip,deflate,sdch");
        header.put("Referer", "http://www.zhihu.com/people/fenng/followees");
        // header.put("Content-Type",
        // "application/x-www-form-urlencoded; charset=UTF-8");
        header.put("Accept", "gzip, deflate");
        header.put("Accept-Language", "	zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        header.put(
                "cookie",
                "   q_c1=ccfd92e359e54617a0ff06a03ca42b92|1424831386000|1421637754000; __utma=51854390.879657291.1421637754.1424831367.1427164906.3; __utmz=51854390.1421637754.1.1.utmcsr=zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/; __utmv=51854390.100--|2=registration_date=20150104=1^3=entry_date=20150104=1; _xsrf=3968f17621eeb31ce6ac15848765bf99; __utmb=51854390.5.10.1427164906; __utmc=51854390; z_c0=\"QUFDQWdQcEdBQUFYQUFBQVlRSlZUZk5iT0ZWZTh2ME01bjhZa0RWY3A2eE5DWnMtUi1TSUxnPT0=|1427164915|ed3101705eb2be2f7c24e05d48faa0b31e0e01fb\"");
        header.put("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        // header.put("X-Requested-With", "XMLHttpRequest");

        return header;
    }

    @Override
    public Object call() throws Exception {
        while (true){
            getResquest();
            TimeUnit.MILLISECONDS.sleep(500);
            switch (downResquest.getFlag()) {
                case InitPAGE:
                    System.out.println("开始下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    DownLoader("http://www.zhihu.com/people/" + downResquest.getUser_data_id(), 3);
                    System.out.println("完成下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    break;
                case AboutPAGE:
                    System.out.println("开始下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    DownLoader("http://www.zhihu.com/people/" + downResquest.getUrl_name() + "/about", 3);
                    System.out.println("完成下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    break;
                case TopicPAGE:
                    System.out.println("开始下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    DownLoader("http://www.zhihu.com/people/" + downResquest.getUrl_name() + "/topics", 3);
                    System.out.println("完成下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    break;
                case ColumnPAGE:
                    System.out.println("开始下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    DownLoader("http://www.zhihu.com/people/" + downResquest.getUrl_name() + "/columns/followed", 3);
                    System.out.println("完成下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    break;
                case FollowerPAGE:
                    System.out.println("开始下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    DownLoader("http://www.zhihu.com/people/" + downResquest.getUrl_name() + "/followers", 3);
                    System.out.println("完成下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    break;
                case FolloweePAGE:
                    System.out.println("开始下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    DownLoader("http://www.zhihu.com/people/" + downResquest.getUrl_name() + "/followees", 3);
                    System.out.println("完成下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    break;
                case AnswerListPAGE:
                    System.out.println("开始下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                    DownLoader("http://www.zhihu.com/people/" + downResquest.getUrl_name() + "/answers?page=" + downResquest.getPage(), 3);
                    System.out.println("完成下载：" + downResquest.getFlag() + " ," + downResquest.getUser_data_id());
                default:
                    System.out.println("未知flag");
            }
        }
    }
}
