package com.example.sp20250610.entity;

import lombok.Data;
import org.apache.tomcat.util.json.JSONFilter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

public class Works {
    private BigInteger id;
    private String username;
    private String title;
    private String category;
    private String description;
    private BigDecimal locationLng;
    private BigDecimal locationLat;
    private String locationAddress;
    private Timestamp createdAt;
    private String role;
    private String cover;
    private BigInteger viewCount;
    private BigInteger commentCount;
    private BigInteger likeCount;
    private String avatar;

    private List<WorkImage> images;

    private String watermarkConfig;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWatermarkConfig() {
        return watermarkConfig;
    }

    public void setWatermarkConfig(String watermarkConfig) {
        this.watermarkConfig = watermarkConfig;
    }


    public BigInteger getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(BigInteger commentCount) {this.commentCount=commentCount;}

    public BigInteger getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(BigInteger likeCount) {this.likeCount=likeCount;}

    public BigInteger getViewCount() {return viewCount;}

    public void setViewCount(BigInteger viewCount) {this.viewCount=viewCount;}

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(BigDecimal locationLng) {
        this.locationLng = locationLng;
    }

    public BigDecimal getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(BigDecimal locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<WorkImage> getImages() {
        return images;
    }

    public void setImages(List<WorkImage> images) {
        this.images = images;
    }
}
