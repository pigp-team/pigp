package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.Goal;
import com.feng.pigp.fans.model.User;

/**
 * @author feng
 * 负责获取用户
 * @date 2019/7/9 10:00
 * @since 1.0
 */
public class UserPoolService {

    /**
     * 这里获取到相同的用户是没有意义的，因此每一个目标都应该维护自己的一个指针
     * 根据goal的指针获取下一个用户
     * @return
     */
    public User getUser(Goal goal){

        return null;
    }
}