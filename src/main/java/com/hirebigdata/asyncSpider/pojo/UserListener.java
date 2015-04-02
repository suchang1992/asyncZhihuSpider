package com.hirebigdata.asyncSpider.pojo;

import com.hirebigdata.asyncSpider.pojo.zhihu.ZhihuUser;

/**
 * Created by Administrator on 2015/3/30.
 */
public class UserListener {
    int count;
    ZhihuUser zhihuUser = new ZhihuUser();

    public UserListener(int count,String user_data_id) {
        this.count = count;
        zhihuUser.setUser_data_id(user_data_id);
    }

    public void setUrl_name(String url_name){
        zhihuUser.setUrl_name(url_name);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incCount(){
        count++;
    }
    public void decCount(){
        count--;
    }
}
