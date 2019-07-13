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

    public SpiderQueryContentNode setKey(String key) {
        this.key = key;
        return this;
    }

    public String getContentXPath() {
        return contentXPath;
    }

    public SpiderQueryContentNode setContentXPath(String contentXPath) {
        this.contentXPath = contentXPath;
        return this;
    }
}