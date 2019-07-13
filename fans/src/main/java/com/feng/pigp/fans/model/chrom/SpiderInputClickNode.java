package com.feng.pigp.fans.model.chrom;

/**
 * @author feng
 * @date 2019/7/10 13:32
 * @since 1.0
 */
public class SpiderInputClickNode extends Node{

    private String triggerXPath;
    private String content;
    private String contentXPath;
    private String clickXPath;

    public String getContent() {
        return content;
    }

    public SpiderInputClickNode setContent(String content) {
        this.content = content;
        return this;
    }

    public String getContentXPath() {
        return contentXPath;
    }

    public SpiderInputClickNode setContentXPath(String contentXPath) {
        this.contentXPath = contentXPath;
        return this;
    }

    public String getClickXPath() {
        return clickXPath;
    }

    public SpiderInputClickNode setClickXPath(String clickXPath) {
        this.clickXPath = clickXPath;
        return this;
    }

    public String getTriggerXPath() {
        return triggerXPath;
    }

    public SpiderInputClickNode setTriggerXPath(String triggerXPath) {
        this.triggerXPath = triggerXPath;
        return this;
    }
}