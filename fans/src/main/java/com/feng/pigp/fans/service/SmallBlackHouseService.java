package com.feng.pigp.fans.service;

import com.feng.pigp.fans.common.EventTypeEnum;
import com.feng.pigp.fans.model.Goal;
import com.feng.pigp.fans.model.MultiGoal;
import com.feng.pigp.fans.model.User;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Map;
import java.util.Set;

@Service
public class SmallBlackHouseService {

    private static int SHARE_LIMIT = 10;
    private static long PERMANENT_VALIDATE = -1;
    private static int COMMENT_BLACKHOUST_TIME_LIMIT = 5*60*1000;
    //转发一天转发10条, 不解除黑
    //回复10条，间隔5分钟，如果出现了频繁，就进小黑屋
    //点赞没有限制
    Map<String, Long> blackHouseMap = Maps.newHashMap();

    public boolean inBlackHouse(User user, Goal goal, EventTypeEnum event){

        switch (event){
            case SHARE:
                Long time = blackHouseMap.get(user.getUsername());
                if(time!=null && time<0){
                    return true;
                }

                if(user.getMessageMetric().getShareCount().intValue() >= SHARE_LIMIT){
                    blackHouseMap.put(user.getUsername()+event.getId(), PERMANENT_VALIDATE);
                    return true;
                }

                return false;
            case COMMENT:
                Long commenTime = blackHouseMap.get(user.getUsername());
                if(commenTime!=null && commenTime<0){
                    return true;
                }

                if(commenTime!=null && (System.currentTimeMillis()-commenTime<COMMENT_BLACKHOUST_TIME_LIMIT)){
                    return true;
                }

                if(user.getMessageMetric().getCommentCount().intValue()>= SHARE_LIMIT){
                    blackHouseMap.put(user.getUsername()+event.getId(), System.currentTimeMillis());
                    return true;
                }

                return false;
            default:
                return false;
        }

    }

    public void addHouse(MultiGoal goal, User user) {

        blackHouseMap.put(user.getUsername()+EventTypeEnum.SHARE, PERMANENT_VALIDATE);
        blackHouseMap.put(user.getUsername()+EventTypeEnum.COMMENT, System.currentTimeMillis());
    }
}