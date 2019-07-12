package com.feng.pigp.fans.model;

import java.util.List;
import java.util.Set;

public class MultiGoal extends Goal{

    //连接形式
    private String url;
    private Set<String> commonIdList;

    public Set<String> getCommonIdList() {
        return commonIdList;
    }

    public void setCommonIdList(Set<String> commonIdList) {
        this.commonIdList = commonIdList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}