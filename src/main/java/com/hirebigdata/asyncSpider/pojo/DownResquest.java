package com.hirebigdata.asyncSpider.pojo;

/**
 * Created by sc on 2015/3/30.
 */
public class DownResquest {
    int flag;
    String user_data_id = "";
    String url_name = "";
    int page;

    public DownResquest(int flag, String user_data_id) {
        this.flag = flag;
        this.user_data_id = user_data_id;
    }

    public DownResquest(int flag, String user_data_id, String url_name) {
        this.flag = flag;
        this.user_data_id = user_data_id;
        this.url_name = url_name;
    }

    public DownResquest(int flag, String user_data_id, String url_name, int page) {
        this.flag = flag;
        this.user_data_id = user_data_id;
        this.url_name = url_name;
        this.page = page;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
