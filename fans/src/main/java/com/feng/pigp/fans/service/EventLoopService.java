package com.feng.pigp.fans.service;

import com.feng.pigp.fans.common.Common;
import com.feng.pigp.fans.model.Goal;
import com.feng.pigp.fans.model.MultiGoal;
import com.feng.pigp.fans.model.SingletonGoal;
import com.feng.pigp.fans.model.User;
import com.feng.pigp.fans.model.chrom.Node;
import com.feng.pigp.fans.model.chrom.SpiderInputClickNode;
import com.feng.pigp.fans.model.chrom.SpiderQueryContentNode;
import com.feng.pigp.fans.model.chrom.SpiderSubElemNumNode;
import com.feng.pigp.util.GsonUtil;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
                List<SingletonGoal> goalList = goalPoolService.getAllGaols();
                for(SingletonGoal goal : goalList){

                    /*if(goal.isFinished()){
                        LOGGER.info("goal has finished", GsonUtil.toJson(goal));
                    }*/

                    //找该goal的用户,开始查找
                    //chromHandlerService.inputAndClick(goal.getUserId());
                    //进入该用户的首页
                    //chromHandlerService.enterUserIndex(goal.getUserId());
                    //处理该用户相关的操作
                    SingletonGoal curGoal = goal;
                    while(curGoal!=null){
                        processOperation(curGoal, user, null);
                        curGoal = (SingletonGoal) goal.getNext();
                    }

                    System.out.println("哈哈哈");

                }

            }
        }catch (Throwable e){
            LOGGER.error("event loop error", e);
        }

    }

    private void processOperation(SingletonGoal goal, User user, Node node) {

        switch (goal.getEventType()){
            case LIKE:
                handlerService.like(goal, user, node);
                break;
            case SHARE:
                handlerService.share(goal, user, node);
                break;
            case ATTENTION:
                handlerService.attention(goal, user, node);
                break;
            case COMMENT:
                handlerService.comment(goal, user, node);
                break;
            default:
                break;
        }
    }


    public void multiRun(){

        MultiGoal goal = new MultiGoal();
        Set<String> commentIdSet = Sets.newHashSet("4391057288197078","4391066558402566");
        goal.setUrl("https://weibo.com/6117570574/HCcOckFXY?from=page_1005056117570574_profile&wvr=6&mod=weibotime");

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

                //转发
                SpiderInputClickNode messageShareNode = new SpiderInputClickNode();
                messageShareNode.setTriggerXPath(Common.MESSAGE_SHARE_FLAG);
                messageShareNode.setClickXPath(Common.MESSAGE_SHARE_SUBMIT);
                messageShareNode.setContentXPath(Common.MESSAGE_SHARE_INPUT);
                messageShareNode.setContent("来来来&*&…"+("^%$#@$T%".getBytes())[RANDOM.nextInt(8)]+"…%……（@"+userName);
                handlerService.share(goal, user, messageShareNode);

                //评论
                SpiderInputClickNode messageCommentNode = new SpiderInputClickNode();
                messageCommentNode.setTriggerXPath(Common.MESSAGE_COMMENT_FLAG);
                messageCommentNode.setClickXPath(Common.MESSAAGE_COMMENT_SUBMIT);
                messageCommentNode.setContentXPath(Common.MESSAGE_COMMENT_INFPUT);
                messageCommentNode.setContent("来来来&*&…"+("^%$#@$T%".getBytes())[RANDOM.nextInt(8)]+"…%……（@"+userName);
                handlerService.comment(goal, user, messageCommentNode);

                //处理评论相关
                if(CollectionUtils.isEmpty(commentIdSet)){
                    continue;
                }

                Set<String> processedSet = Sets.newHashSet();
                int index = 1;
                while(processedSet.size()<commentIdSet.size()) {

                    SpiderQueryContentNode commentIdNode = new SpiderQueryContentNode();
                    commentIdNode.setContentXPath(String.format(Common.COMMENT_TOTOLE_FLAG, index));
                    commentIdNode.setKey("comment_id");
                    String commentId = handlerService.getCommentId(commentIdNode);
                    if(StringUtils.isEmpty(commentId)){
                        //还是没有找到，加载更多
                        SpiderQueryContentNode moreNode = new SpiderQueryContentNode();
                        moreNode.setContentXPath(Common.COMMENT_TOTAL_MORE);
                        handlerService.click(moreNode);
                        continue;
                    }

                    index++;
                    if (commentIdSet.contains(commentId)) {
                        LOGGER.info("find comment :{}", commentId);
                        //开始处理评论
                        processedSet.add(commentId);
                        //处理该条评论
                        //1. 获取首楼，判断是否自评
                        SpiderQueryContentNode commentNode = new SpiderQueryContentNode();
                        commentNode.setContentXPath(String.format(Common.COMMENT_FIRST_USERNAME, index));
                        String firstUserName = handlerService.getCommentId(commentNode);
                        if(userName.equals(firstUserName)){
                            LOGGER.info("self comment {}-{}", user.getUsername(), goal.getUserName());
                            //自评， 回复+点赞
                            //先不管时间
                            SpiderQueryContentNode firstCommentNode = new SpiderQueryContentNode();
                            firstCommentNode.setContentXPath(String.format(Common.COMMENT_FIRST_LIKE, index));
                            handlerService.like(goal, user, firstCommentNode);
                            //回复
                            SpiderInputClickNode firstCommentReplayNode = new SpiderInputClickNode();
                            firstCommentReplayNode.setTriggerXPath(String.format(Common.COMMENT_FIRST_COMMENT, index));
                            firstCommentReplayNode.setClickXPath(String.format(Common.COMMENT_FIRST_COMMENT_SUBMIT, index));
                            firstCommentReplayNode.setContentXPath(String.format(Common.COMMENT_FIRST_COMMENT_INPUT, index));
                            firstCommentReplayNode.setContent("来来来&*&…"+("^%$#@$T%".getBytes())[RANDOM.nextInt(8)]+"…%……（@"+userName);
                            handlerService.comment(goal, user, firstCommentReplayNode);
                        }

                        //楼中楼 回复+点赞
                        //获取共有回复数
                        int subCommentCount = 100;
                        for(int i=1; i<=subCommentCount; ){

                            //1. 获取子评论姓名
                            SpiderQueryContentNode subCommentNode = new SpiderQueryContentNode();
                            subCommentNode.setContentXPath(String.format(Common.COMMENT_SUB_USERNAME, index, i));
                            String subUserName = handlerService.getCommentId(subCommentCount);
                            if(StringUtils.isEmpty(subUserName)){
                                //点击更多
                                continue;
                            }

                            i++;
                            //2.  自评 + 点赞
                            LOGGER.info("ABA comment {}-{}", user.getUsername(), goal.getUserName());
                            //自评， 回复+点赞
                            //先不管时间
                            SpiderQueryContentNode subLikeNode = new SpiderQueryContentNode();
                            subLikeNode.setContentXPath(String.format(Common.COMMENT_SUB_LIKE, index, i));
                            handlerService.like(goal, user, subLikeNode);
                            //回复
                            SpiderInputClickNode subCommentReplayNode = new SpiderInputClickNode();
                            subCommentReplayNode.setTriggerXPath(String.format(Common.COMMENT_SUB_COMMENT, index, i));
                            subCommentReplayNode.setClickXPath(String.format(Common.COMMENT_FIRST_COMMENT_SUBMIT, index, i));
                            subCommentReplayNode.setContentXPath(String.format(Common.COMMENT_FIRST_COMMENT_INPUT, index, i));
                            subCommentReplayNode.setContent("来来来&*&…"+("^%$#@$T%".getBytes())[RANDOM.nextInt(8)]+"…%……（@"+userName);
                            handlerService.comment(goal, user, subCommentCount);
                        }
                        continue;
                    }

                    LOGGER.info("find comment not match : {}", commentId);
                }
            }catch (Throwable e){
                LOGGER.error("run is error", e);
            }
        }
    }
}