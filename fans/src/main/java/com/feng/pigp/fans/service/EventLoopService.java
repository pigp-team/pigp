package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.Goal;
import com.feng.pigp.fans.model.MultiGoal;
import com.feng.pigp.fans.model.User;
import com.feng.pigp.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author feng
 * @date 2019/7/10 9:44
 * @since 1.0
 */
@Service
public class EventLoopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoopService.class);
    @Resource
    private GoalPoolService goalPoolService;
    @Resource
    private UserPoolService userPoolService;
    @Resource
    private HandlerService handlerService;
    /**
     * 为了避免账号的切换，登录上账号之后，该账号可以做所有的事情
     */
    public void singletonRun(){

        try{

            for(;;) {
                //1. 获取用户
                User user = userPoolService.getUser();
                if (user == null) {
                    LOGGER.error("event loop query user error");
                    break;
                }

                //登录
                handlerService.login(user);

                //获取所有的任务
                List<Goal> goalList = goalPoolService.getAllGaols();
                for(Goal goal : goalList){

                    /*if(goal.isFinished()){
                        LOGGER.info("goal has finished", GsonUtil.toJson(goal));
                    }*/

                    //找该goal的用户,开始查找
                    //chromHandlerService.inputAndClick(goal.getUserId());
                    //进入该用户的首页
                    //chromHandlerService.enterUserIndex(goal.getUserId());
                    //处理该用户相关的操作
                    Goal curGoal = goal;
                    while(curGoal!=null){
                        processOperation(curGoal);
                        curGoal = goal.getNext();
                    }

                    System.out.println("哈哈哈");

                }

            }
        }catch (Throwable e){
            LOGGER.error("event loop error", e);
        }

    }

    private void processOperation(Goal curGoal) {

        /*switch (curGoal.getEventType()){
            case LIKE:
                chromHandlerService.like(curGoal);
                break;
            case SHARE:
                break;
            case ATTENTION:
                break;
            case COMMENT:
                break;
            default:
                break;
        }*/
    }


    public void multiRun(){

        MultiGoal goal = new MultiGoal();
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
            String userName = handlerService.openUrlAndGetUser(goal, user, null);

            //关注
            handlerService.fullAttention();

            //点赞
            handlerService.fullLike();

            break;
        }
    }
}