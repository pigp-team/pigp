package com.feng.pigp.fans.model.chrom;/**
 * Created by feng on 2019/7/11.
 */

/**
 * @author feng
 * @date 2019/7/11 10:12
 * @since 1.0
 */
public class SpiderMatchClickNode extends Node{

    private String matchContent;
    private String contentXPath;
    private String clickXPath;
    private int stratIndex;
    private int endIndex;

    private String clickContent; //匹配才点击，例如如果已经点赞了，那么就不点了
    private String clickKey;

    public int getStratIndex() {
        return stratIndex;
    }

    public void setStratIndex(int stratIndex) {
        this.stratIndex = stratIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public String getMatchContent() {
        return matchContent;
    }

    public void setMatchContent(String matchContent) {
        this.matchContent = matchContent;
    }

    public String getContentXPath() {
        return contentXPath;
    }

    public void setContentXPath(String contentXPath) {
        this.contentXPath = contentXPath;
    }

    public String getClickXPath() {
        return clickXPath;
    }

    public void setClickXPath(String clickXPath) {
        this.clickXPath = clickXPath;
    }

    public String getClickContent() {
        return clickContent;
    }

    public void setClickContent(String clickContent) {
        this.clickContent = clickContent;
    }

    public String getClickKey() {
        return clickKey;
    }

    public void setClickKey(String clickKey) {
        this.clickKey = clickKey;
    }
}