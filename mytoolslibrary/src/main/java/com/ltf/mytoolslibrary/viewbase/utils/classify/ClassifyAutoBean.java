package com.ltf.mytoolslibrary.viewbase.utils.classify;

/**
 * 作者：${李堂飞} on 2016/10/31 0031 09:55
 * 邮箱：1195063924@qq.com
 * 注解: 自动生成分屏分类 携带数据的Model
 */
public class ClassifyAutoBean {

    private String img;
    private String title;
    private String id;
    private String ShopId;//店铺ID
    private String id_fu;//父级id
    private String cls_id;//数据分类id
    private String url;
    private int width = 0;
    private int height = 0;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getCls_id() {
        return cls_id;
    }

    public void setCls_id(String cls_id) {
        this.cls_id = cls_id;
    }

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String shopId) {
        ShopId = shopId;
    }

    public String getId_fu() {
        return id_fu;
    }

    public void setId_fu(String id_fu) {
        this.id_fu = id_fu;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
