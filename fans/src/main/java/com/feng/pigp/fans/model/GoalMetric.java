package com.feng.pigp.fans.model;

import java.util.concurrent.atomic.LongAdder;

public class GoalMetric {

    //统计相关
    private LongAdder AttentionCount = new LongAdder();
    private LongAdder LikeCount = new LongAdder();
    private LongAdder CommentCount = new LongAdder();
    private LongAdder ShareCount = new LongAdder();

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