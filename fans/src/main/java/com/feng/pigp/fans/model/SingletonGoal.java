package com.feng.pigp.fans.model;

import com.feng.pigp.fans.common.EventTypeEnum;

public class SingletonGoal extends Goal{


    private EventTypeEnum eventType;
    private int countLimit;
    private String matchContent;

    public EventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeEnum eventType) {
        this.eventType = eventType;
    }

    public int getCountLimit() {
        return countLimit;
    }

    public void setCountLimit(int countLimit) {
        this.countLimit = countLimit;
    }

    public String getMatchContent() {
        return matchContent;
    }

    public void setMatchContent(String matchContent) {
        this.matchContent = matchContent;
    }
}