package com.feng.pigp.fans.service;

import com.feng.pigp.fans.common.EventTypeEnum;
import com.feng.pigp.fans.model.Goal;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author feng
 * @date 2019/7/10 9:29
 * @since 1.0
 */
@Service
public class GoalPoolService {

    private static Logger LOGGER = LoggerFactory.getLogger(GoalPoolService.class);
    private LinkedBlockingQueue<Goal> goals = new LinkedBlockingQueue<>(100);

    //可以提交任务
    public boolean submitGoalTask(Goal goal){

        return goals.offer(goal);
    }

    //可以获取任务
    public Goal getGoalTask(){
        try {
            return goals.take();
        } catch (InterruptedException e) {
            LOGGER.error("tak goals error", e);
        }

        return null;
    }

    public List<Goal> getAllGaols() {

        List<Goal> result = Lists.newArrayList();
        Goal goal = new Goal();
        goal.setEventTypeEnum(EventTypeEnum.LIKE);
        goal.setCountLimit(10);
        goals.drainTo(result);
        result.add(goal);
        return result;
    }
}