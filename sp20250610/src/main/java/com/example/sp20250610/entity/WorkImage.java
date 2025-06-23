package com.example.sp20250610.entity;

import lombok.Data;
import java.math.BigInteger;


public class WorkImage {
    private BigInteger id;
    private BigInteger workId;
    private String imagePath;
    private Boolean isCover;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getWorkId() {
        return workId;
    }

    public void setWorkId(BigInteger workId) {
        this.workId = workId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Boolean getCover() {
        return isCover;
    }

    public void setCover(Boolean cover) {
        isCover = cover;
    }
}