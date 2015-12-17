package com.julun.business.beans;

import android.graphics.Bitmap;

/**
 * cs
 */
public class TestBean {
	private Integer id;
	private String title;
	private String imageUrl;
	private String info;
	private Integer marketPrice;
	private Integer buyCount;
	private Integer buyPrice;
	private Bitmap bitmap;
	
	public TestBean(int id, String title, String info, String imageUrl){
		super();
		this.id = id;
		this.title = title;
		this.imageUrl = imageUrl;
		this.info = info;
	}
    public TestBean(){}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Integer getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Integer marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Integer getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
	}

	public Integer getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Integer buyPrice) {
		this.buyPrice = buyPrice;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
