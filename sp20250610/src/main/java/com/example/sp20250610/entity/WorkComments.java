package com.example.sp20250610.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

public class WorkComments {
    private BigInteger id;
    private BigInteger workId;
    private BigInteger userId;
    private String username;
    private String comment;
    private Timestamp commentTime;
    private String avatar;
    private String workTitle;

    public Timestamp getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Timestamp commentTime) {
        this.commentTime = commentTime;
    }

    private Boolean isDeleted;
    private BigInteger parentCommentId;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public BigInteger getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(BigInteger parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }
}
