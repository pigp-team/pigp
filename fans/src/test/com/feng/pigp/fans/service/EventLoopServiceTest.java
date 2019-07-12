package com.feng.pigp.fans.service;

import com.feng.pigp.fans.common.EventTypeEnum;
import com.feng.pigp.fans.model.Goal;
import com.feng.pigp.fans.model.MultiGoal;
import com.feng.pigp.fans.model.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
    @Resource
    private UserPoolService userPoolService;

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

        MultiGoal goal1 = new MultiGoal();
        goal1.setUrl("https://weibo.com/6117570574/HD2Yd8eu3?from=page_1005056117570574_profile&wvr=6&mod=weibotime");
        goal1.setCommonIdList(Sets.newHashSet("4393062250095139", "4393062329823837", "4393062442592593", "4393062643718480"));

        MultiGoal goal2 = new MultiGoal();
        goal2.setUrl("https://weibo.com/6117570574/HD00t2WKA?from=page_1005056117570574_profile&wvr=6&mod=weibotime");
        goal2.setCommonIdList(Sets.newHashSet("4392959862160159", "4392959925487040", "4392960499897871", "4392961112179868", "4392961511196701"));

        MultiGoal goal3 = new MultiGoal();
        goal3.setUrl("https://weibo.com/6117570574/HCXanvhcg?from=page_1005056117570574_profile&wvr=6&mod=weibotime");
        goal3.setCommonIdList(Sets.newHashSet("4392839176045888", "4392840350510272", "4392839947428496", "4392839259637109", "4392839695663314"));

        MultiGoal goal4 = new MultiGoal();
        goal4.setUrl("https://weibo.com/6117570574/HCQxhdKiG?from=page_1005056117570574_profile&wvr=6&mod=weibotime");
        goal4.setCommonIdList(Sets.newHashSet("4392584275189727", "4392600611719334", "4392600494589372", "4392604847883966"));

        MultiGoal goal5 = new MultiGoal();
        goal5.setUrl("https://weibo.com/6117570574/HCP2toA0v?from=page_1005056117570574_profile&wvr=6&mod=weibotime");
        goal5.setCommonIdList(Sets.newHashSet("4392526825483397", "4392526896928651", "4392527018823965", "4392527614079443", "4392527710485668"));

        MultiGoal goal6 = new MultiGoal();
        goal6.setUrl("https://weibo.com/6117570574/HCO7YDwFk?from=page_1005056117570574_profile&wvr=6&mod=weibotime");
        goal6.setCommonIdList(Sets.newHashSet("4392491773710202", "4392492151474957", "4392491824172517", "4392492340010905", "4392492512223384"));


        goalPoolService.submitMulti(goal1);
        goalPoolService.submitMulti(goal2);
        goalPoolService.submitMulti(goal3);
        goalPoolService.submitMulti(goal4);
        goalPoolService.submitMulti(goal5);
        goalPoolService.submitMulti(goal6);

        User user1 = new User();
        user1.setUsername("ixxztuadncgiyh-per@yahoo.com");
        user1.setPwd("Kgnqksdmq3");

        User user2 = new User();
        user2.setUsername("etvfgojhpdklu-upl@yahoo.com");
        user2.setPwd("Ffzlkaagp2");

        User user3 = new User();
        user3.setUsername("ndmljycpjwdxon-cj16324@yahoo.com");
        user3.setPwd("EAqdrmqqdnd81");

        User user4 = new User();
        user4.setUsername("iqullyfirrcqun-mmur9@yahoo.com");
        user4.setPwd("Hajldlzlc3");

        User user5 = new User();
        user5.setUsername("ecjhkhjspksvmy-bwhg@yahoo.com");
        user5.setPwd("Dzjyyhdwc4");

        userPoolService.addUser(user1);
        userPoolService.addUser(user2);
        userPoolService.addUser(user3);
        userPoolService.addUser(user4);
        userPoolService.addUser(user5);

        eventLoopService.multiRun();
    }
}