package com.feng.spider.model;

import com.feng.spider.constant.SpiderEventEnum;

import java.util.List;

/**
 * @author feng
 * @date 2019/3/12 10:17
 * @since 1.0
 */
public class SpiderTransferWindowsEventNode extends SpiderEventNode{

    private List<String> excludeWindows;

    public SpiderTransferWindowsEventNode() {
        eventEnum = SpiderEventEnum.TRANSFER_WINDOWS;
    }
}