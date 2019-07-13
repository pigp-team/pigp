package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.User;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    boolean isInit = false;
    public List<List<User>> batchList = Lists.newArrayList();

    @PostConstruct
    public void init() throws IOException {

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(File);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

        String line = null;
        while((line=br.readLine())!=null){
            int index = line.indexOf("=");
            if(index<0){
                continue;
            }

            User user = new User();
            user.setUsername(line.substring(0, index));
            user.setPwd(line.substring(index+1, line.length()));
            userList.add(user);
        }
        LOGGER.info("load user finish : {}", userList.size());
    }

    public List<List<User>> getUser(int size){

        List<List<User>> result = Lists.newArrayList();
        for(int i=0; i<size; i++){
            result.add(Lists.newArrayList());
        }

        for(int i=0; i<userList.size(); i++){
            result.get(i%size).add(userList.get(i));
        }
        return result;
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