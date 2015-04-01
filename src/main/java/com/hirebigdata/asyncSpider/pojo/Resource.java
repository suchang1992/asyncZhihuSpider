package com.hirebigdata.asyncSpider.pojo;

/**
 * Created by Administrator on 2015/3/30.
 */
public class Resource {
    int flag;
    String user_data_id;
    String url_name;
    String data;

    public Resource(int flag, String user_data_id, String url_name) {
        this.flag = flag;
        this.user_data_id = user_data_id;
        this.url_name = url_name;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getUser_data_id() {
        return user_data_id;
    }

    public void setUser_data_id(String user_data_id) {
        this.user_data_id = user_data_id;
    }

    public String getUrl_name() {
        return url_name;
    }

    public void setUrl_name(String url_name) {
        this.url_name = url_name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
