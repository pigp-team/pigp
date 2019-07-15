package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.Goal;
import com.feng.pigp.fans.model.User;
import com.feng.pigp.fans.model.chrom.SpiderQueryContentNode;
import com.feng.pigp.fans.model.chrom.SpiderSubElemNumNode;

/**
 * @author feng
 * @date 2019/7/10 11:25
 * @since 1.0
 */
public interface HandlerService<T> {

    boolean login(User user);

    boolean inputAndClick(String userId);

    boolean enterUserIndex(String userId);

    boolean like(Goal goal, User user, T object);

    boolean share(Goal goal, User user, T object);

    boolean comment(Goal goal, User user, T object);

    boolean collection(Goal goal, User user, T object);

    boolean attention(Goal goal, User user, T object);

    boolean logout(User user);

    boolean isLogin();

    String openUrlAndGetUser(Goal goal, User user, T object);

    int getCommentCount(T  object);

    String getContent(T object);

    void click(T object);

    void clickWithScollBottom(T object);

    void close();

    void refresh();
}