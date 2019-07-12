package com.feng.pigp.fans.model;

import com.feng.pigp.fans.common.EventTypeEnum;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author feng
 * @date 2019/7/10 9:25
 * @since 1.0
 */
public class Goal {

    private String userName;
    private int id;
    private Goal next;

    private GoalMetric messageMetric;
    private GoalMetric selfCommentMetric;
    private GoalMetric commentMetric;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Goal getNext() {
        return next;
    }

    public void setNext(Goal next) {
        this.next = next;
    }
}