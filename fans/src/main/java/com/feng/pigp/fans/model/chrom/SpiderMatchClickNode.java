package com.feng.pigp.fans.model.chrom;/**
 * Created by feng on 2019/7/11.
 */

/**
 * @author feng
 * @date 2019/7/11 10:12
 * @since 1.0
 */
public class SpiderMatchClickNode {

    private String matchContent;
    private String contentXPath;
    private String clickXPath;
    private int stratIndex;
    private int endIndex;

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
}