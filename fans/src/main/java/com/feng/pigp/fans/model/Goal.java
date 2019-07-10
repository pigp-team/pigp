package com.feng.pigp.fans.model;

import com.feng.pigp.fans.common.EventTypeEnum;

/**
 * @author feng
 * @date 2019/7/10 9:25
 * @since 1.0
 */
public class Goal {

    private EventTypeEnum eventTypeEnum;
    private int countLimit;

    public EventTypeEnum getEventTypeEnum() {
        return eventTypeEnum;
    }

    public void setEventTypeEnum(EventTypeEnum eventTypeEnum) {
        this.eventTypeEnum = eventTypeEnum;
    }

    public int getCountLimit() {
        return countLimit;
    }

    public void setCountLimit(int countLimit) {
        this.countLimit = countLimit;
    }
}