package com.hirebigdata.asyncSpider.thread;

import com.hirebigdata.asyncSpider.pojo.DownResquest;
import com.hirebigdata.asyncSpider.pojo.Resource;
import com.hirebigdata.asyncSpider.pojo.UserListener;
import com.hirebigdata.asyncSpider.pojo.zhihu.Skilled_topic;
import com.hirebigdata.asyncSpider.pojo.zhihu.ZhihuUser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by sc on 2015/3/30.
 */
public class DealThread implements Callable {
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

    LinkedBlockingQueue<DownResquest> down_queue;
    LinkedBlockingQueue<Resource> deal_queue;
    ConcurrentHashMap<String, UserListener> userMap;

    Resource resource;

    public DealThread(LinkedBlockingQueue<DownResquest> down_queue, LinkedBlockingQueue<Resource> deal_queue, ConcurrentHashMap<String, UserListener> userMap) {
        this.down_queue = down_queue;
        this.deal_queue = deal_queue;
        this.userMap = userMap;
    }

    private void error(String user_data_id) {
        System.out.println("error"+ resource.getFlag() + " ," + resource.getUser_data_id());
        userMap.get(user_data_id).decCount();
    }

    private void success(String user_data_id) {
        userMap.get(user_data_id).decCount();
    }

    private void putDownResquest(int flag, String user_data_id) {
        try {
            down_queue.put(new DownResquest(flag, resource.getUser_data_id()));
            userMap.get(resource.getUser_data_id()).incCount();
        } catch (InterruptedException e) {
            System.out.println("入队出错");
            e.printStackTrace();
        }
    }

    private void putDownResquest(int flag, String user_data_id, String url_name) {
        try {
            System.out.println("提交下载请求：" + flag + " ," + resource.getUser_data_id()+" ,"+url_name);
            down_queue.put(new DownResquest(flag, user_data_id, url_name));
            userMap.get(resource.getUser_data_id()).incCount();
        } catch (InterruptedException e) {
            System.out.println("入队出错");
            e.printStackTrace();
        }
    }

    private void putDownResquest(int flag, String user_data_id, String url_name, int page) {
        try {
            down_queue.put(new DownResquest(flag, user_data_id, url_name, page));
            userMap.get(resource.getUser_data_id()).incCount();
            System.out.println("提交下载请求：" + flag + " ," + resource.getUser_data_id()+" ,"+url_name);
        } catch (InterruptedException e) {
            System.out.println("入队出错");
            e.printStackTrace();
        }
    }

    private void getRessource() {
        try {
            resource = deal_queue.take();
        } catch (InterruptedException e) {
            System.out.println("获得资源出错");
            e.printStackTrace();
        }

    }


    private void deal_init_html() {
        Document page = Jsoup.parse(resource.getData());
        String user_data_id = resource.getUser_data_id();
        String s = page.getElementsByTag("meta").get(5).attr("content");
        String url_name = new String(s.getBytes(), 45, s.length() - 45);
        resource.setUrl_name(url_name); //为资源赋值url_name属性
        ZhihuUser zhihuUser = userMap.get(user_data_id).getZhihuUser();
        zhihuUser.setUrl_name(url_name);//获得url_name
        putDownResquest(AboutPAGE, user_data_id, url_name);//提交about下载需求

        Elements els = page.getElementsByAttributeValue("class", "profile-navbar clearfix").first().getElementsByClass("item");
        zhihuUser.setQuestions_count(els.get(1).getElementsByTag("span").first().text());//获得question count
        zhihuUser.setAnswers_count(els.get(2).getElementsByTag("span").first().text());//获得answer count
        zhihuUser.setPosts_count(els.get(3).getElementsByTag("span").first().text());//获得专栏 count
        zhihuUser.setCollections_count(els.get(4).getElementsByTag("span").first().text());//获得 收藏 count
        zhihuUser.setLogs_count(els.get(5).getElementsByTag("span").first().text());//获得 公共编辑 count

        //分析 专栏
//        System.out.println("分析专栏");
        Element e = page.getElementsByAttributeValue("href", "/people/" + url_name + "/columns/followed").first();
        if (e != null) {
            String columns_count = e.text();
            zhihuUser.setFollow_columns_count(columns_count);
            if (Integer.parseInt(columns_count.replace(" 个专栏", "")) > 0)//专栏数>1 提出页面下载需求
                putDownResquest(ColumnPAGE, user_data_id, url_name);
        }
        //分析 话题
//        System.out.println("分析话题");
        e = page.getElementsByAttributeValue("href", "/people/" + url_name + "/topics").first();
        if (e != null) {
            String topics_count = e.text();
            zhihuUser.setFollow_topics_count(topics_count);
            if (Integer.parseInt(topics_count.replace(" 个话题", "")) > 0)//话题数>1 提出页面下载需求
                putDownResquest(TopicPAGE, user_data_id, url_name);
        }

        els = page.getElementsByAttributeValue("class", "zg-gray-normal");
        //关注了 count
//        System.out.println("分析关注了");
        e = els.first();
        int Followee_count = Integer.parseInt(e.parent().getElementsByTag("strong").first().text());
        zhihuUser.setFollowee_count("" + Followee_count);
        if (Followee_count > 0)//关注了 >0 提出页面下载需求
            putDownResquest(FolloweePAGE, user_data_id, url_name);
        //关注者 count
//        System.out.println("分析关注者");
        e = els.get(1);
        int Follower_count = Integer.parseInt(e.parent().getElementsByTag("strong").first().text());
        zhihuUser.setFollower_count("" + Follower_count);
        if (Follower_count > 0)//关注者 >0 提出页面下载需求
            putDownResquest(FollowerPAGE, user_data_id, url_name);
//        System.out.println("分析页尾");
        e = els.get(2);
        zhihuUser.setPersonal_page_view_count(e.getElementsByTag("strong").first().text());//得到主页被浏览次数
        zhihuUser.setUser__xsrf_value(page.getElementsByAttributeValue("name", "_xsrf").first().attr("value"));//得到该id的 _xsrf
        //获得用户头像
//        System.out.println("分析头像");
        e = page.getElementsByAttributeValue("class", "zm-profile-header-img zg-avatar-big zm-avatar-editor-preview").first();
        if (e != null)
            zhihuUser.setAvatar(e.attr("src"));


        //得到擅长话题
//        System.out.println("分析擅长话题");
        e = page.getElementsByAttributeValue("class", "zm-profile-section-list zg-clear").first();
        if (e != null) {
            els = e.getElementsByAttributeValue("class", "item");
            for (Element element : els) {
                Skilled_topic skilled_topic = new Skilled_topic();
                skilled_topic.setName(element.getElementsByAttributeValue("class", "zg-gray-darker").text());
                skilled_topic.setUrl(element.attr("data-url-token"));
                skilled_topic.setVote(element.getElementsByAttributeValue("class", "zg-icon vote").first().parent().text());
                skilled_topic.setComment(element.getElementsByAttributeValue("class", "zg-icon comment").first().parent().text());
                zhihuUser.getSkilled_topics().add(skilled_topic);
            }
        }
        int Answers_list_count = (int) Math.ceil(Double.parseDouble(zhihuUser.getAnswers_count()) / 20.0);
        for (int i = 1; i <= Answers_list_count; i++)//提出 回答列表页 下载请求
            putDownResquest(AnswerListPAGE, user_data_id, url_name, i);
//        System.out.println("init finish");
        success(user_data_id);//init页面解析完成
    }

    private void deal_about_html() {
        Document page = Jsoup.parse(resource.getData());
    }

    @Override
    public Integer call() throws Exception {
        while(true) {
            getRessource();
            //处理资源
            switch (resource.getFlag()) {
                case InitPAGE:
                    System.out.println("处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
                    deal_init_html();
                    System.out.println("完成处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
                    break;
                case AboutPAGE:
                    System.out.println("处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
                    deal_about_html();
                    System.out.println("完成处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
                    break;
                case TopicPAGE:
                    System.out.println("处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
//                    deal_about_html();
                    System.out.println("完成处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
                    break;
                case ColumnPAGE:
                    System.out.println("处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
//                    deal_about_html();
                    System.out.println("完成处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
                    break;
                case FollowerPAGE:
                    System.out.println("处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
//                    deal_about_html();
                    System.out.println("完成处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
                    break;
                case FolloweePAGE:
                    System.out.println("处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
//                    deal_about_html();
                    System.out.println("完成处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
                    break;
                case AnswerListPAGE:
                    System.out.println("处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
//                    deal_about_html();
                    System.out.println("完成处理资源：" + resource.getFlag() + " ," + resource.getUser_data_id());
                    break;
                default:
                    System.out.println("error" + resource.getFlag() + " ," + resource.getUser_data_id());
                    error(resource.getUser_data_id());
            }

        }
    }
}
