package com.feng.pigp.fans.service;

import com.feng.pigp.fans.common.Common;
import com.feng.pigp.fans.model.MultiGoal;
import com.feng.pigp.fans.model.User;
import com.feng.pigp.fans.model.chrom.SpiderInputClickNode;
import com.feng.pigp.fans.model.chrom.SpiderQueryContentNode;
import com.feng.pigp.fans.util.ToolUtil;
import com.feng.pigp.util.GsonUtil;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author feng
 * @date 2019/7/10 9:44
 * @since 1.0
 */
@Service
public class EventLoopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoopService.class);
    private static final int MAX_COMMENT_COUNT = 50;
    private static final int MAX_SUB_COMMONT_COUNT = 20;
    private static final long MAX_COMMENT_INTERVAL = 5*60*1000;
    private static final int MAX_COMMENT_INTERVAL_COUNT = 15;
    private static final String COMMENT_ID_KEY = "comment_id";

    ThreadLocal<Long> isComment = new ThreadLocal<>();
    ThreadLocal<Integer> commentCount = new ThreadLocal<>();

    @Resource
    private GoalPoolService goalPoolService;
    @Resource
    private UserPoolService userPoolService;
    @Resource
    private HandlerService handlerService;
    @Resource
    private CommentPoolService commentPoolService;


    /**
     * 主流程方法
     */
    public void run(){

        boolean isFirst = true;
        for(;;) {

            User user = userPoolService.getUser();
            if (user == null) {
                LOGGER.error("event loop query user error");
                break;
            }
            LOGGER.info("user : {} start work", user.getUsername());

            try {
                //1. 获取用户
                if(!isFirst) {
                    LOGGER.info("switch user:{}", user.getUsername());
                    //切换用户
                    handlerService.logout(user);
                }

                isFirst = false;
                //登录
                handlerService.login(user);
                List<MultiGoal> goalList = goalPoolService.getMulti();
                for(MultiGoal goal : goalList) {
                    processMultiGoal(goal, user);
                }
            }catch (Throwable e){
                LOGGER.error("run is error : {}", user.getUsername(), e);
                isFirst = true;
                handlerService.close();
            }
        }

        handlerService.close();
    }

    /**
     * user处理goal
     * @param goal
     * @param user
     */
    private void processMultiGoal(MultiGoal goal, User user) {

        String userName = goal.getUserName();

        //2.打开连接
        String curUserName = handlerService.openUrlAndGetUser(goal, user, new SpiderQueryContentNode().setContentXPath(Common.FULL_COMMENT_USERNAME));
        LOGGER.info("start comment : {}-{}", userName, curUserName);
        if (StringUtils.isEmpty(userName)) {
            LOGGER.error("open goal url fail {}-{}", GsonUtil.toJson(goal), user.getUsername());
            return;
        }

        String topic = handlerService.getCommentId(new SpiderQueryContentNode().setContentXPath(Common.SINA_TOPIC_MESSAGE));
        processTopic(goal, user, topic);
        processComment(goal, user,topic);
    }

    /**
     * user处理goal的评论
     * @param user
     * @param goal
     * @param topic
     */
    private void processComment(MultiGoal goal, User user,String topic) {

        int moreCount = 0;
        for (int index=1; index < MAX_COMMENT_COUNT;) {

            updateCommentLimit();
            ToolUtil.sleep(500);
            String commentId = handlerService.getCommentId(new SpiderQueryContentNode().setContentXPath(String.format(Common.COMMENT_TOTOLE_FLAG, index)).setKey(COMMENT_ID_KEY));

            //还是没有找到，加载更多
            if (StringUtils.isEmpty(commentId)) {
                moreCount++;
                if(moreCount >= 10){
                    break;
                }
                handlerService.clickWithScollBottom(new SpiderQueryContentNode().setContentXPath(Common.COMMENT_TOTAL_MORE));
                continue;
            }

            //1. 获取首楼，判断是否自评
            String firstUserName = handlerService.getCommentId(new SpiderQueryContentNode().setContentXPath(String.format(Common.COMMENT_FIRST_USERNAME, index)));
            if (goal.getUserName().equals(firstUserName)) {//自评， 回复+点赞

                LOGGER.info("self comment {}-{}", user.getUsername(), goal.getUserName());
                boolean likeSuccess = handlerService.like(goal, user, new SpiderQueryContentNode().setContentXPath(String.format(Common.COMMENT_FIRST_LIKE, index)));
                if(!likeSuccess){
                    index++;
                    continue;
                }

                if(isComment.get()!=null && isComment.get()-System.currentTimeMillis()<=MAX_COMMENT_INTERVAL){
                    index++;
                    continue;
                }
                handlerService.comment(goal, user, new SpiderInputClickNode()
                        .setTriggerXPath(String.format(Common.COMMENT_FIRST_COMMENT, index))
                        .setClickXPath(String.format(Common.COMMENT_FIRST_COMMENT_SUBMIT, index))
                        .setContentXPath(String.format(Common.COMMENT_FIRST_COMMENT_INPUT, index))
                        .setContent(commentPoolService.queryCommentWithInternalComment(goal, topic)));
                commentCount.set((commentCount.get()!=null?commentCount.get():0)+1);
            }

            //楼中楼 回复+点赞
            processSubComment(goal, user, topic, index);
            index++;
        }
    }

    private void updateCommentLimit() {

       if(commentCount.get()!=null && commentCount.get()>MAX_COMMENT_INTERVAL_COUNT){
           commentCount.set(0);
           isComment.set(System.currentTimeMillis());
       }
    }

    /**
     * user处理goal的index评论的子评论
     * @param goal
     * @param user
     * @param topic
     * @param index
     */
    private void processSubComment(MultiGoal goal, User user, String topic, int index) {

        Set<String> hasProcessId = Sets.newHashSet();
        for (int i = 1; i <= MAX_SUB_COMMONT_COUNT; ) {
            String subCommentId = handlerService.getCommentId(new SpiderQueryContentNode()
                    .setContentXPath(String.format(Common.COMMENT_SUB_TOTOLE_FLAG, index, i)).setKey(COMMENT_ID_KEY));

            //查看更多
            if (StringUtils.isEmpty(subCommentId)) {
                //handlerService.click(new SpiderQueryContentNode().setContentXPath(String.format(Common.COMMENT_SUB_MORE, index, i)));
                break; //不展开，因为调研感觉展开没有内容
            }

            if (hasProcessId.contains(subCommentId)) {
                i++;
                continue;
            }

            //1. 获取子评论姓名
            String subUserName = handlerService.getCommentId(new SpiderQueryContentNode().setContentXPath(String.format(Common.COMMENT_SUB_USERNAME, index, i)));
            if (!goal.getUserName().equals(subUserName)) {
                i++;
                continue;
            }

            //2.  自评 + 点赞
            LOGGER.info("ABA comment {}-{}", user.getUsername(), goal.getUserName());
            boolean likeSuccess = handlerService.like(goal, user, new SpiderQueryContentNode().setContentXPath(String.format(Common.COMMENT_SUB_LIKE, index, i)));
            if(!likeSuccess){
                i++;
                continue;
            }
            //回复
            if(isComment.get()!=null && isComment.get()-System.currentTimeMillis()<=MAX_COMMENT_INTERVAL){
                i++;
                continue;
            }

            handlerService.comment(goal, user, new SpiderInputClickNode()
                    .setTriggerXPath(String.format(Common.COMMENT_SUB_COMMENT, index, i))
                    .setClickXPath(String.format(Common.COMMENT_SUB_COMMENT_SUBMIT, index, i))
                    .setContentXPath(String.format(Common.COMMENT_SUB_COMMENT_INPUT, index, i))
                    .setContent(commentPoolService.queryCommentWithInternalComment(goal, topic)));
            commentCount.set((commentCount.get()!=null?commentCount.get():0)+1);

            hasProcessId.add(subCommentId);
            i++;
        }
    }

    /**
     * user处理goal的消息
     * @param user
     * @param goal
     * @param topic
     */
    private void processTopic(MultiGoal goal, User user, String topic) {

        //关注
        handlerService.attention(goal, user, new SpiderQueryContentNode().setContentXPath(Common.FULL_COMMENT_ATTENTION));
        //点赞
        handlerService.like(goal, user, new SpiderQueryContentNode().setContentXPath(Common.Full_LIKE));
        //转发
        handlerService.share(goal, user,
                new SpiderInputClickNode().setTriggerXPath(Common.MESSAGE_SHARE_FLAG)
                        .setClickXPath(Common.MESSAGE_SHARE_SUBMIT)
                        .setContentXPath(Common.MESSAGE_SHARE_INPUT)
                        .setContent(commentPoolService.queryCommentWithShare(goal, topic)));
        //评论
        handlerService.comment(goal, user,
                new SpiderInputClickNode().setTriggerXPath(Common.MESSAGE_COMMENT_FLAG)
                        .setClickXPath(Common.MESSAAGE_COMMENT_SUBMIT)
                        .setContentXPath(Common.MESSAGE_COMMENT_INFPUT)
                        .setContent(commentPoolService.queryCommentWithComment(goal, topic)));
        commentCount.set((commentCount.get()!=null?commentCount.get():0)+1);
    }
}