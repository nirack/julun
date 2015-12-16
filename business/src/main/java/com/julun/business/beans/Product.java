package com.julun.business.beans;

public class Product {
    private Integer id;
    private String name;
    private String type;

    private String typeName;
    private String baseImgUrl;
    private String price;

    private Integer rate;// 星星
    private boolean tuan;// 团购标记
    private String servScope;// 服务范围
    private String location;// 地址

    public Product() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getBaseImgUrl() {
        return baseImgUrl;
    }

    public void setBaseImgUrl(String baseImgUrl) {
        this.baseImgUrl = baseImgUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public boolean isTuan() {
        return tuan;
    }

    public void setTuan(boolean tuan) {
        this.tuan = tuan;
    }

    public String getServScope() {
        return servScope;
    }

    public void setServScope(String servScope) {
        this.servScope = servScope;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
