package com.feng.spider.model;

import com.feng.spider.constant.SpiderEventEnum;
import org.openqa.selenium.WebDriver;

/**
 * @author feng
 * @date 2019/3/11 16:28
 * @since 1.0
 */
public class SpiderEventNode {

    protected SpiderEventEnum eventEnum;
    protected SpiderEventNode next;

    public SpiderEventNode getNext() {
        return next;
    }

    public void setNext(SpiderEventNode next) {
        this.next = next;
    }

    public SpiderEventEnum getEventEnum() {
        return eventEnum;
    }

    public void setEventEnum(SpiderEventEnum eventEnum) {
        this.eventEnum = eventEnum;
    }
}