package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.Goal;
import com.feng.pigp.fans.model.SingletonGoal;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @author feng
 * @date 2019/7/10 9:29
 * @since 1.0
 */
@Service
public class GoalPoolService {

    private static Logger LOGGER = LoggerFactory.getLogger(GoalPoolService.class);

    private Map<String, Goal> user2Goal = Maps.newHashMap();
    private List<Goal> goalList = Lists.newArrayList();

    //可以提交任务
    public synchronized boolean submitGoalTask(Goal goal){

        Goal temp = user2Goal.get(goal.getUserName());
        if(temp==null){
            goalList.add(goal);
            user2Goal.put(goal.getUserName(), goal);
            return true;
        }

        temp.setNext(goal);
        return true;
    }

    public synchronized List<SingletonGoal> getAllGaols() {
        return Lists.newArrayList();
    }
}