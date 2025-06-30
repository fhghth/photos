package com.example.sp20250610.entity;

import java.math.BigInteger;
import java.security.Timestamp;

public class WorkLikes {
    private BigInteger id;
    private BigInteger workId;
    private BigInteger userId;
    private Timestamp liketime;

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

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public Timestamp getLiketime() {
        return liketime;
    }

    public void setLiketime(Timestamp liketime) {
        this.liketime = liketime;
    }
}
