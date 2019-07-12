package com.feng.pigp.fans.model.chrom;

/**
 * @author feng
 * @date 2019/7/12 9:41
 * @since 1.0
 */
public class SpiderQueryContentNode extends Node {

    private String contentXPath;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContentXPath() {
        return contentXPath;
    }

    public void setContentXPath(String contentXPath) {
        this.contentXPath = contentXPath;
    }
}