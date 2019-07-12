package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.Goal;
import com.feng.pigp.fans.model.User;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author feng
 * 负责获取用户
 * @date 2019/7/9 10:00
 * @since 1.0
 */
@Service
public class UserPoolService {

    /**
     * 这里获取到相同的用户是没有意义的，因此每一个目标都应该维护自己的一个指针
     * 根据goal的指针获取下一个用户
     * @return
     */
    public static AtomicInteger index = new AtomicInteger(0);
    public List<User> userList = Lists.newArrayList();

    public User getUser(){

        if(index.get()<userList.size()) {
            return userList.get(index.getAndIncrement());
        }

        return null;
    }

    public void addUser(User user){
        userList.add(user);
    }
}