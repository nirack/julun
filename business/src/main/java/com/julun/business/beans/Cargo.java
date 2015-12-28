package com.julun.business.beans;

/**
 * Created by Administrator on 2015-12-10.
 */
public class Cargo {

    private Integer seqId;

    private Integer userId;

    /**
     * 产品ID
     */
    private int productId;
    /**
     * 产品名
     */
    private String productName;
    /**
     * 单价.
     */
    private Float price ;
    /**
     * 数量
     */
    private int count;

    /**
     * 产品图片
     */
    private String imageUrl;

    public Integer getSeqId() {
        return seqId;
    }

    public void setSeqId(Integer seqId) {
        this.seqId = seqId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
