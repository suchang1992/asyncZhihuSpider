package com.hirebigdata.asyncSpider.pojo;

import com.hirebigdata.asyncSpider.pojo.zhihu.ZhihuUser;

/**
 * Created by sc on 2015/3/30.
 */
public class UserListener {
    int count;
    ZhihuUser zhihuUser = new ZhihuUser();

    public UserListener(int count,String user_data_id) {
        this.count = count;
        zhihuUser.setUser_data_id(user_data_id);
    }

    public int getCount() {
        return count;
    }

    public void incCount(){
        count++;
    }
    public void decCount(){
        count--;
    }

    public ZhihuUser getZhihuUser() {
        return zhihuUser;
    }
}
