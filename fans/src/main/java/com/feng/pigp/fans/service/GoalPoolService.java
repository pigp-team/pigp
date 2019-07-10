package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.Goal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author feng
 * @date 2019/7/10 9:29
 * @since 1.0
 */
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
}