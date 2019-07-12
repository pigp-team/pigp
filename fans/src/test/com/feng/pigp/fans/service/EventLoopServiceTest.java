package com.feng.pigp.fans.service;

import com.feng.pigp.fans.common.EventTypeEnum;
import com.feng.pigp.fans.model.Goal;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author feng
 * @date 2019/7/10 11:59
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-context.xml"})
public class EventLoopServiceTest {

    @Resource
    private EventLoopService eventLoopService;
    @Resource
    private GoalPoolService goalPoolService;

    @Test
    public void run(){
        Goal goal = new Goal();
        /*goal.setUserId("Logic-Luo");
        goal.setEventType(EventTypeEnum.LIKE);
        goal.setMatchContent("#哈哈哈# 哈哈");
        goal.setCountLimit(10);*/
        goalPoolService.submitGoalTask(goal);
        eventLoopService.singletonRun();
    }

    @Test
    public void multiRun() throws Exception {
        eventLoopService.multiRun();
    }
}