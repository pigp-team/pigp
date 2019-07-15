package com.feng.pigp.fans.model;

import java.time.LocalDateTime;

/**
 * @author feng
 * @date 2019/7/10 9:32
 * @since 1.0
 */
public class User {

    private int id;
    private String username;
    private String pwd;
    private int taskId;
    private String status;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;

    private GoalMetric messageMetric = new GoalMetric();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public GoalMetric getMessageMetric() {
        return messageMetric;
    }

    public void setMessageMetric(GoalMetric messageMetric) {
        this.messageMetric = messageMetric;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", pwd='" + pwd + '\'' +
                ", messageMetric=" + messageMetric.toString() +
                '}';
    }
}