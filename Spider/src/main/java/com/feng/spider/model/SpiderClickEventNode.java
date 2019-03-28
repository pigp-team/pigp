package com.feng.spider.model;

import com.feng.spider.constant.SpiderEventEnum;

/**
 * @author feng
 * @date 2019/3/11 17:27
 * @since 1.0
 */
public class SpiderClickEventNode extends SpiderEventNode{


    private String matchXPath;
    private String matchContent;
    private String loadMoreXPath;
    private String clickXPath;

    public SpiderClickEventNode() {
        eventEnum = SpiderEventEnum.CLICK;
    }

    public String getLoadMoreXPath() {
        return loadMoreXPath;
    }

    public void setLoadMoreXPath(String loadMoreXPath) {
        this.loadMoreXPath = loadMoreXPath;
    }

    public String getMatchXPath() {
        return matchXPath;
    }

    public void setMatchXPath(String matchXPath) {
        this.matchXPath = matchXPath;
    }

    public String getMatchContent() {
        return matchContent;
    }

    public void setMatchContent(String matchContent) {
        this.matchContent = matchContent;
    }

    public String getClickXPath() {
        return clickXPath;
    }

    public void setClickXPath(String clickXPath) {
        this.clickXPath = clickXPath;
    }
}