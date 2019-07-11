package com.feng.pigp.fans.common;

/**
 * @author feng
 * @date 2019/7/10 9:26
 * @since 1.0
 */
public enum EventTypeEnum {

    LIKE(1),   //点赞
    SHARE(2), //分享
    COMMENT(4), //评论
    ATTENTION(8), //关注
    COLLECTION(16); //收藏

    private int id;

    EventTypeEnum(int id) {
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
}