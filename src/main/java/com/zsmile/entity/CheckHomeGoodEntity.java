package com.zsmile.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 检索首页商品列表
 *
 * @author BetraySmile
 * @email zzz
 * @date 2020-04-27 14:53:08
 */
public class CheckHomeGoodEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 检索id
     */
    private Long checkId;
    /**
     * 名称
     */
    private String rawTitle;
    /**
     * 显示价格
     */
    private BigDecimal viewPrice;
    /**
     * 省市区
     */
    private String itemLoc;
    /**
     * 详情URL
     */
    private String detailUrl;
    /**
     * 评论URL
     */
    private String commentUrl;
    /**
     * 店铺/昵称
     */
    private String nick;
    /**
     * 标题
     */
    private String title;
    /**
     * 收货人
     */
    private String viewSales;
    /**
     * 收货人
     */
    private Integer viewSales2;
    /**
     * 店铺URL
     */
    private String shopLink;
    /**
     * 图片URL
     */
    private String picUrl;

    /**
     * 设置：检索id
     */
    public void setCheckId(Long checkId) {
        this.checkId = checkId;
    }

    /**
     * 获取：检索id
     */
    public Long getCheckId() {
        return checkId;
    }

    /**
     * 设置：名称
     */
    public void setRawTitle(String rawTitle) {
        this.rawTitle = rawTitle;
    }

    /**
     * 获取：名称
     */
    public String getRawTitle() {
        return rawTitle;
    }

    /**
     * 设置：显示价格
     */
    public void setViewPrice(BigDecimal viewPrice) {
        this.viewPrice = viewPrice;
    }

    /**
     * 获取：显示价格
     */
    public BigDecimal getViewPrice() {
        return viewPrice;
    }

    /**
     * 设置：省市区
     */
    public void setItemLoc(String itemLoc) {
        this.itemLoc = itemLoc;
    }

    /**
     * 获取：省市区
     */
    public String getItemLoc() {
        return itemLoc;
    }

    /**
     * 设置：详情URL
     */
    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    /**
     * 获取：详情URL
     */
    public String getDetailUrl() {
        return detailUrl;
    }

    /**
     * 设置：评论URL
     */
    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }

    /**
     * 获取：评论URL
     */
    public String getCommentUrl() {
        return commentUrl;
    }

    /**
     * 设置：店铺/昵称
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * 获取：店铺/昵称
     */
    public String getNick() {
        return nick;
    }

    /**
     * 设置：标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取：标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置：收货人
     */
    public void setViewSales(String viewSales) {
        this.viewSales = viewSales;
    }

    /**
     * 获取：收货人
     */
    public String getViewSales() {
        return viewSales;
    }

    /**
     * 设置：店铺URL
     */
    public void setShopLink(String shopLink) {
        this.shopLink = shopLink;
    }

    /**
     * 获取：店铺URL
     */
    public String getShopLink() {
        return shopLink;
    }

    /**
     * 设置：图片URL
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    /**
     * 获取：图片URL
     */
    public String getPicUrl() {
        return picUrl;
    }

    public Integer getViewSales2() {
        return viewSales2;
    }

    public void setViewSales2(Integer viewSales2) {
        this.viewSales2 = viewSales2;
    }

}
