package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.Goal;
import com.feng.pigp.fans.model.User;
import com.feng.pigp.fans.model.chrom.SpiderInputClickNode;

/**
 * @author feng
 * @date 2019/7/10 11:25
 * @since 1.0
 */
public interface HandlerService {

    boolean login(User user);

    boolean inputAndClick(String userId);

    boolean enterUserIndex(String userId);

    boolean like(Goal curGoal);
}