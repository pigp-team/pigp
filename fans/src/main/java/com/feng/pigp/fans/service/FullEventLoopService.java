package com.feng.pigp.fans.service;/**
 * Created by feng on 2019/7/11.
 */

import com.feng.pigp.fans.model.FullGoal;
import com.feng.pigp.fans.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author feng
 * @date 2019/7/11 19:47
 * @since 1.0
 */
@Service
public class FullEventLoopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullEventLoopService.class);
    @Resource
    private HandlerService handlerService;
    @Resource
    private UserPoolService userPoolService;

    public void run(){

        FullGoal goal = new FullGoal();
        goal.setUrl("https://weibo.com/6117570574/HC56wFQBN?from=page_1005056117570574_profile&wvr=6&mod=weibotime");

        for(;;) {
            //1. 获取用户
            User user = userPoolService.getUser();
            if (user == null) {
                LOGGER.error("event loop query user error");
                break;
            }

            //登录
            handlerService.login(user);

            //2.打开连接
            String userName = handlerService.openUrlAndGetUser(goal);

            //关注
            handlerService.fullAttention();

            //点赞
            handlerService.fullLike();

            break;
        }
    }
}