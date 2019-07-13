package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.User;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

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
    private static final Logger LOGGER  = LoggerFactory.getLogger(UserPoolService.class);

    public static AtomicInteger index = new AtomicInteger(0);
    public List<User> userList = Lists.newArrayList();
    private static final String File = "user.pwd";

    @PostConstruct
    public void init() throws IOException {

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(File);
        Properties properties = new Properties();
        properties.load(inputStream);

        for(Map.Entry<Object,Object> entry : properties.entrySet()){

            User user = new User();
            user.setUsername((String)entry.getKey());
            if(user.getUsername().startsWith("#")){
                continue;
            }
            user.setPwd((String)entry.getValue());
            userList.add(user);
        }
        LOGGER.info("load user finish : {}", userList.size());
    }

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