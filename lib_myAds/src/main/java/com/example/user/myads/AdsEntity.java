package com.example.user.myads;

import java.io.Serializable;


/**
 *
 * 暂未使用，留作拓展
 *
 */
public class AdsEntity implements Serializable {
    private String dec;
    private String url;

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
