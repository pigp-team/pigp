package com.feng.pigp.fans.model;

public class UserMetric {

    private GoalMetric messageMetric;
    private GoalMetric selfCommentMetric;
    private GoalMetric commentMetric;

    public GoalMetric getMessageMetric() {
        return messageMetric;
    }

    public void setMessageMetric(GoalMetric messageMetric) {
        this.messageMetric = messageMetric;
    }

    public GoalMetric getSelfCommentMetric() {
        return selfCommentMetric;
    }

    public void setSelfCommentMetric(GoalMetric selfCommentMetric) {
        this.selfCommentMetric = selfCommentMetric;
    }

    public GoalMetric getCommentMetric() {
        return commentMetric;
    }

    public void setCommentMetric(GoalMetric commentMetric) {
        this.commentMetric = commentMetric;
    }
}