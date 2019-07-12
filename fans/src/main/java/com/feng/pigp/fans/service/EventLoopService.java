package com.feng.pigp.fans.service;

import com.feng.pigp.fans.common.Common;
import com.feng.pigp.fans.model.Goal;
import com.feng.pigp.fans.model.MultiGoal;
import com.feng.pigp.fans.model.User;
import com.feng.pigp.fans.model.chrom.SpiderInputClickNode;
import com.feng.pigp.fans.model.chrom.SpiderQueryContentNode;
import com.feng.pigp.util.GsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

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

    private static final Random RANDOM = new Random();
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
            try {
                //1. 获取用户
                User user = userPoolService.getUser();
                if (user == null) {
                    LOGGER.error("event loop query user error");
                    break;
                }

                //登录
                handlerService.login(user);

                //2.打开连接
                SpiderQueryContentNode queryNode = new SpiderQueryContentNode();
                queryNode.setContentXPath(Common.FULL_COMMENT_USERNAME);
                String userName = handlerService.openUrlAndGetUser(goal, user, queryNode);
                if (StringUtils.isEmpty(userName)) {
                    LOGGER.error("open goal url fail {}-{}", GsonUtil.toJson(goal), user.getUsername());
                    continue;
                }

                //更新目标用户的信息
                goal.setUserName(userName);

                //关注
                SpiderQueryContentNode attentionNode = new SpiderQueryContentNode();
                attentionNode.setContentXPath(Common.FULL_COMMENT_ATTENTION);
                handlerService.attention(goal, user, attentionNode);

                //点赞
                SpiderQueryContentNode messageLikeNode = new SpiderQueryContentNode();
                messageLikeNode.setContentXPath(Common.Full_LIKE);
                handlerService.like(goal, user, messageLikeNode);

                //评论
                SpiderInputClickNode messageCommentNode = new SpiderInputClickNode();
                messageCommentNode.setTriggerXPath(Common.MESSAGE_COMMENT_FLAG);
                messageCommentNode.setClickXPath(Common.MESSAAGE_COMMENT_SUBMIT);
                messageCommentNode.setContentXPath(Common.MESSAGE_COMMENT_INFPUT);
                messageCommentNode.setContent("来来来&*&…"+("^%$#@$T%".getBytes())[RANDOM.nextInt(8)]+"…%……（@"+userName);
                handlerService.comment(goal, user, messageCommentNode);

                //转发
                SpiderInputClickNode messageShareNode = new SpiderInputClickNode();
                messageShareNode.setTriggerXPath(Common.MESSAGE_SHARE_FLAG);
                messageShareNode.setClickXPath(Common.MESSAGE_SHARE_SUBMIT);
                messageShareNode.setContentXPath(Common.MESSAGE_SHARE_INPUT);
                messageShareNode.setContent("来来来&*&…"+("^%$#@$T%".getBytes())[RANDOM.nextInt(8)]+"…%……（@"+userName);
                handlerService.share(goal, user, messageShareNode);
            }catch (Throwable e){
                LOGGER.error("run is error", e);
            }
        }
    }
}