package com.feng.pigp.fans.model;

import java.util.concurrent.atomic.LongAdder;

public class GoalMetric {

    //统计相关
    private LongAdder AttentionCount;
    private LongAdder LikeCount;
    private LongAdder CommentCount;
    private LongAdder ShareCount;

    public LongAdder getAttentionCount() {
        return AttentionCount;
    }

    public void setAttentionCount(LongAdder attentionCount) {
        AttentionCount = attentionCount;
    }

    public LongAdder getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(LongAdder likeCount) {
        LikeCount = likeCount;
    }

    public LongAdder getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(LongAdder commentCount) {
        CommentCount = commentCount;
    }

    public LongAdder getShareCount() {
        return ShareCount;
    }

    public void setShareCount(LongAdder shareCount) {
        ShareCount = shareCount;
    }
}