package com.feng.spider.model;

import com.feng.spider.constant.SpiderEventEnum;

/**
 * @author feng
 * @date 2019/3/12 10:56
 * @since 1.0
 */
public class SpiderOperationEventNode extends SpiderEventNode {

    //工作小目录
    private String namespace;
    //文章更新的篇数
    private String articleNumXPath;
    //文章页的下一篇文章
    private String nextArticleXPath;
    //文章列表页遍历到的当前文章
    private String curArticleXPath;
    private String curArticleEnterXPath;
    //文章标题XPath
    private String articleTitleXPath;
    //被挤下线是出现的错误提示，防止制作的图片中出现弹窗遮挡
    private String errorXPath;
    //文章从第几篇开始
    private int curNum = 1;

    public SpiderOperationEventNode() {
        eventEnum = SpiderEventEnum.OPERATION;
    }

    public String getArticleNumXPath() {
        return articleNumXPath;
    }

    public void setArticleNumXPath(String articleNumXPath) {
        this.articleNumXPath = articleNumXPath;
    }

    public String getNextArticleXPath() {
        return nextArticleXPath;
    }

    public void setNextArticleXPath(String nextArticleXPath) {
        this.nextArticleXPath = nextArticleXPath;
    }

    public String getCurArticleXPath() {
        return curArticleXPath;
    }

    public void setCurArticleXPath(String curArticleXPath) {
        this.curArticleXPath = curArticleXPath;
    }

    public String getCurArticleEnterXPath() {
        return curArticleEnterXPath;
    }

    public void setCurArticleEnterXPath(String curArticleEnterXPath) {
        this.curArticleEnterXPath = curArticleEnterXPath;
    }

    public String getErrorXPath() {
        return errorXPath;
    }

    public void setErrorXPath(String errorXPath) {
        this.errorXPath = errorXPath;
    }

    public int getCurNum() {
        return curNum;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    public String getArticleTitleXPath() {
        return articleTitleXPath;
    }

    public void setArticleTitleXPath(String articleTitleXPath) {
        this.articleTitleXPath = articleTitleXPath;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}