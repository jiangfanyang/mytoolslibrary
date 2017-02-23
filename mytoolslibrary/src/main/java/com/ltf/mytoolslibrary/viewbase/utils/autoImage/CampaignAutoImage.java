package com.ltf.mytoolslibrary.viewbase.utils.autoImage;

/**
 * 作者：${李堂飞} on 2016/11/3 0003 14:30
 * 邮箱：1195063924@qq.com
 * 注解: 活动列表图片 自适应服务器 点击图片跳转WAP 本类是携带数据的Model
 */
public class CampaignAutoImage {

    private String title;
    private String id;//活动ID
    private String cpn_id;//公司ID
    private String  shop_id;//店铺ID
    private String type;//点击跳转类型（0:WAP;1:原生）
    private String imgPath;
    private String url;

    public String getCpn_id() {
        return cpn_id;
    }

    public void setCpn_id(String cpn_id) {
        this.cpn_id = cpn_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
