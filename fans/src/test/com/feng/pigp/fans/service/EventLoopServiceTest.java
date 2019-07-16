package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.MultiGoal;
import com.feng.pigp.fans.model.User;
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

    @Test
    public void multiRun() throws Exception {

        eventLoopService.runBatch(1);
    }

    @Test
    public void mobileTest(){
        User user = new User();
        MultiGoal goal = new MultiGoal();
        eventLoopService.enterInteractivePage(user, goal);
    }
}