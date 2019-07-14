package com.feng.pigp.fans.model;

/**
 * @author feng
 * @date 2019/7/10 9:32
 * @since 1.0
 */
public class User {

    private String username;
    private String pwd;

    private GoalMetric messageMetric = new GoalMetric();


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