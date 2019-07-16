package com.feng.pigp.fans.service;

import com.feng.pigp.fans.common.Common;
import com.feng.pigp.fans.common.EventTypeEnum;
import com.feng.pigp.fans.exception.AccountErrorException;
import com.feng.pigp.fans.exception.FansException;
import com.feng.pigp.fans.model.MultiGoal;
import com.feng.pigp.fans.model.User;
import com.feng.pigp.fans.model.chrom.SpiderInputClickNode;
import com.feng.pigp.fans.model.chrom.SpiderQueryContentNode;
import com.feng.pigp.fans.util.ToolUtil;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author feng
 * @date 2019/7/10 9:44
 * @since 1.0
 */
@Service
public class EventLoopService{

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoopService.class);
    private static final int MAX_COMMENT_COUNT = 10;
    private static final int MAX_SUB_COMMONT_COUNT = 5;
    private static final String COMMENT_ID_KEY = "comment_id";

    @Resource
    private GoalPoolService goalPoolService;
    @Resource
    private UserPoolService userPoolService;
    @Resource
    private HandlerService handlerService;
    @Resource
    private CommentPoolService commentPoolService;
    @Resource
    private SmallBlackHouseService smallBlackHouseService;


    public void runBatch(int batchSize){

        final CountDownLatch countLatch = new CountDownLatch(batchSize);
        List<List<User>> list = userPoolService.getUser(batchSize);
        for(List<User> user : list){

            new Thread(new Task(this, user, countLatch)).start();
        }

        try {
            countLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("countdown error", e);
        }

    }

    public static class Task implements Runnable{
        private EventLoopService eventLoopService;
        private List<User> userList;
        private CountDownLatch countLatch;

        public Task(EventLoopService eventLoopService,
                    List<User> userList, CountDownLatch countLatch) {
            this.eventLoopService = eventLoopService;
            this.userList = userList;
            this.countLatch = countLatch;
        }

        @Override
        public void run() {
            this.eventLoopService.run(userList);
            countLatch.countDown();
        }
    }

    /**
     * 主流程方法
     */
    public void run(List<User> userList){

        boolean isFirst = true;
        for(User user : userList) {

            long start = System.currentTimeMillis();
            int goalCount = 0;
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
                boolean isFirstGoal = true;

                //检测互动值
                for(MultiGoal goal : goalList) {
                    long goalStart = System.currentTimeMillis();
                    try {
                        enterInteractivePage(user, goal);
                        goalCount++;
                        processMultiGoal(goal, user, isFirstGoal);
                        isFirstGoal = false;
                    }catch (FansException e){
                        LOGGER.error("process error :{}-{}-{}", user.getUsername(), goal.getUserName(), goal.getUrl(), e);
                    }finally {
                        LOGGER.info("process url :{}-{}-{}", goalCount, goal.getUrl(), (System.currentTimeMillis()-goalStart));
                    }
                }
            }catch (AccountErrorException e){
                //切换账号
                continue;
            } catch (Throwable e){
                LOGGER.error("run is error account is error : {}", user.getUsername(), e);
                handlerService.refresh();
            }finally {
                LOGGER.info("user finish time : {}-{}-{}-{}", user.getUsername(), goalCount,
                        user, (System.currentTimeMillis()-start));
            }
        }

        handlerService.close();
    }

    public void enterInteractivePage(User user, MultiGoal goal) {

        handlerService.switchWindowns();
        handlerService.refresh();
        handlerService.click(new SpiderQueryContentNode().setContentXPath(Common.M_SINA_INDEX));
        handlerService.click(new SpiderQueryContentNode().setContentXPath(Common.M_SINA_INTERACTIVE));
        String yestValue = handlerService.getContent(new SpiderQueryContentNode().setContentXPath(Common.M_LAST_VALUE));
        String allValue = handlerService.getContent(new SpiderQueryContentNode().setContentXPath(Common.M_ALL_VALUE));
        handlerService.switchWindowns();
        LOGGER.info("#### user interactive value #### :{}-{}-{}-{} ！！！", user.getUsername(), goal.getUserName(), yestValue, allValue);
    }

    /**
     * user处理goal
     * @param goal
     * @param user
     * @param isFirstGoal
     */
    private void processMultiGoal(MultiGoal goal, User user, boolean isFirstGoal) {

        String userName = goal.getUserName();

        //2.打开连接
        String curUserName = handlerService.openUrlAndGetUser(goal, user, new SpiderQueryContentNode().setContentXPath(Common.FULL_COMMENT_USERNAME));
        LOGGER.info("start comment : {}-{}", userName, curUserName);
        if (StringUtils.isEmpty(curUserName)) {
            LOGGER.error("open goal url fail {}-{}", goal.getUrl(), user.getUsername());
            return;
        }

        String topic = handlerService.getContent(new SpiderQueryContentNode().setContentXPath(Common.SINA_TOPIC_MESSAGE));

        if(goal.getUserName().equals(curUserName)) {
            processTopic(goal, user, topic, isFirstGoal);
        }
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

            validate(goal, user);
            ToolUtil.sleep(500);
            String commentId = handlerService.getContent(new SpiderQueryContentNode().setContentXPath(String.format(Common.COMMENT_TOTOLE_FLAG, index)).setKey(COMMENT_ID_KEY));

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
            String firstUserName = handlerService.getContent(new SpiderQueryContentNode().setContentXPath(String.format(Common.COMMENT_FIRST_USERNAME, index)));
            if (goal.getUserName().equals(firstUserName)) {//自评， 回复+点赞

                LOGGER.info("self comment {}-{}", user.getUsername(), goal.getUserName());
                boolean likeSuccess = handlerService.like(goal, user, new SpiderQueryContentNode().setContentXPath(String.format(Common.COMMENT_FIRST_LIKE, index)));
                if(!likeSuccess){
                    index++;
                    continue;
                }

                if(!smallBlackHouseService.inBlackHouse(user, goal, EventTypeEnum.COMMENT)) {
                    handlerService.comment(goal, user, new SpiderInputClickNode()
                            .setTriggerXPath(String.format(Common.COMMENT_FIRST_COMMENT, index))
                            .setClickXPath(String.format(Common.COMMENT_FIRST_COMMENT_SUBMIT, index))
                            .setContentXPath(String.format(Common.COMMENT_FIRST_COMMENT_INPUT, index))
                            .setContent(commentPoolService.queryCommentWithInternalComment(goal, topic)));
                }
            }

            //楼中楼 回复+点赞
            processSubComment(goal, user, topic, index);
            index++;
        }
    }

    private void validate(MultiGoal goal, User user) {

        String content = handlerService.getContent(new SpiderQueryContentNode().setContentXPath(Common.COMMENT_ERROR_LIMIT));
        if(StringUtils.isEmpty(content)){
            return;
        }

        LOGGER.error("validate info message : $$$$$${}-{}$$$$$", user.getUsername(), content);

        //点击
        handlerService.click(new SpiderQueryContentNode().setContentXPath(Common.COMMENT_ERROR_LIMIT));
        //进入小黑屋，老实一点
        smallBlackHouseService.addHouse(goal, user);
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
            String subCommentId = handlerService.getContent(new SpiderQueryContentNode()
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
            String subUserName = handlerService.getContent(new SpiderQueryContentNode().setContentXPath(String.format(Common.COMMENT_SUB_USERNAME, index, i)));
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

            if(!smallBlackHouseService.inBlackHouse(user, goal, EventTypeEnum.COMMENT)) {
                handlerService.comment(goal, user, new SpiderInputClickNode()
                        .setTriggerXPath(String.format(Common.COMMENT_SUB_COMMENT, index, i))
                        .setClickXPath(String.format(Common.COMMENT_SUB_COMMENT_SUBMIT, index, i))
                        .setContentXPath(String.format(Common.COMMENT_SUB_COMMENT_INPUT, index, i))
                        .setContent(commentPoolService.queryCommentWithInternalComment(goal, topic)));
            }

            hasProcessId.add(subCommentId);
            i++;
        }
    }

    /**
     * user处理goal的消息
     * @param goal
     * @param user
     * @param topic
     * @param isFirstGoal
     */
    private void processTopic(MultiGoal goal, User user, String topic, boolean isFirstGoal) {

        //判断是否关注：这里非常重要，如果没有关注，将会前功尽弃
        if(isFirstGoal) {
            //关注
            handlerService.attention(goal, user, new SpiderQueryContentNode().setContentXPath(Common.FULL_COMMENT_ATTENTION));
        }else{
            //判断是否关注
            String message = handlerService.getContent(new SpiderQueryContentNode().setContentXPath(Common.FULL_COMMENT_ATTENTION));
            LOGGER.info("attention info : {}-{}", user.getUsername(), message);
            if(message!=null && !"已关注".equals(message)){
                LOGGER.error("======================{} has not attention ================", user.getUsername());
            }
            handlerService.attention(goal, user, new SpiderQueryContentNode().setContentXPath(Common.FULL_COMMENT_ATTENTION));

        }

        //点赞
        handlerService.like(goal, user, new SpiderQueryContentNode().setContentXPath(Common.Full_LIKE));

        //转发
        if(!smallBlackHouseService.inBlackHouse(user, goal, EventTypeEnum.SHARE)) {
            handlerService.share(goal, user,
                    new SpiderInputClickNode().setTriggerXPath(Common.MESSAGE_SHARE_FLAG)
                            .setClickXPath(Common.MESSAGE_SHARE_SUBMIT)
                            .setContentXPath(Common.MESSAGE_SHARE_INPUT)
                            .setContent(commentPoolService.queryCommentWithShare(goal, topic)));
        }

        //评论
        if(!smallBlackHouseService.inBlackHouse(user, goal, EventTypeEnum.COMMENT)) {
            handlerService.comment(goal, user,
                    new SpiderInputClickNode().setTriggerXPath(Common.MESSAGE_COMMENT_FLAG)
                            .setClickXPath(Common.MESSAAGE_COMMENT_SUBMIT)
                            .setContentXPath(Common.MESSAGE_COMMENT_INFPUT)
                            .setContent(commentPoolService.queryCommentWithComment(goal, topic)));
        }else{
            handlerService.click(new SpiderQueryContentNode().setContentXPath(Common.MESSAGE_COMMENT_FLAG));
        }
    }
}