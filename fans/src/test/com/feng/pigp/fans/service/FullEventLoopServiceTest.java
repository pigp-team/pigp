package com.feng.pigp.fans.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by feng on 2019/7/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-context.xml"})
public class FullEventLoopServiceTest {

    @Resource
    private FullEventLoopService fullEventLoopService;
    @Resource
    private GoalPoolService goalPoolService;

    @Test
    public void run() throws Exception {
        fullEventLoopService.run();
    }

}