package com.ltf.mytoolslibrary.viewbase.base.quicksearch;

import java.io.Serializable;

/**
 * 作者：${李堂飞} on 2016/10/25 0025 15:05
 * 邮箱：1195063924@qq.com
 * 注解: 智能搜索JavaBean
 */
public class dataBean implements Serializable{

    private String name;
    private String id;
    private String lat;//纬度
    private String lng;//经度
    private String code;
    private String city;

    public dataBean(String name, String id, String lng, String lat, String code, String city) {
        this.name = name;
        this.id = id;
        this.lng = lng;
        this.lat = lat;
        this.code = code;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
