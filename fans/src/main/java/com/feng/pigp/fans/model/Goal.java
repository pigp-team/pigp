package com.feng.pigp.fans.model;

import com.feng.pigp.fans.common.EventTypeEnum;

import java.util.List;

/**
 * @author feng
 * @date 2019/7/10 9:25
 * @since 1.0
 */
public class Goal {

    private EventTypeEnum eventType;
    private int countLimit;
    private int curCount;

    private String userId;
    private String matchContent;

    private Goal next;

    public int getCurCount() {
        return curCount;
    }

    public void setCurCount(int curCount) {
        this.curCount = curCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public EventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeEnum eventType) {
        this.eventType = eventType;
    }

    public String getMatchContent() {
        return matchContent;
    }

    public void setMatchContent(String matchContent) {
        this.matchContent = matchContent;
    }

    public int getCountLimit() {
        return countLimit;
    }

    public void setCountLimit(int countLimit) {
        this.countLimit = countLimit;
    }

    public boolean isFinished(){
        return this.curCount>=this.countLimit;
    }

    public Goal getNext() {
        return next;
    }

    public void setNext(Goal next) {
        this.next = next;
    }
}