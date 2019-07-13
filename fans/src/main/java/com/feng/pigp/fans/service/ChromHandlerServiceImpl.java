package com.feng.pigp.fans.service;

import com.feng.pigp.fans.common.Common;
import com.feng.pigp.fans.exception.FansException;
import com.feng.pigp.fans.model.Goal;
import com.feng.pigp.fans.model.MultiGoal;
import com.feng.pigp.fans.model.User;
import com.feng.pigp.fans.model.chrom.*;
import com.feng.pigp.fans.util.ChromDriverSpiderUtil;
import com.feng.pigp.fans.util.ToolUtil;
import com.feng.pigp.util.GsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author feng
 * @date 2019/7/10 11:25
 * @since 1.0
 */
@Service
public class ChromHandlerServiceImpl implements HandlerService<Node> {

    private static final Logger LOGGER = LoggerFactory.getLogger("fans");

    private static final int MAX_RETRY = 3;
    private ThreadLocal<WebDriver> threadLocal = new ThreadLocal<WebDriver>();

    @Override
    public boolean login(User user) {

        LOGGER.info("handler service login start {}", user.getUsername());
        if(threadLocal.get()!=null){
            logout(user);
        }

        loginWithOutLogout(user, 1);
        LOGGER.info("handler service login success {}", user.getUsername());
        return true;
    }

    private void loginWithOutLogout(User user, int retryNum) {

        if(retryNum>=MAX_RETRY){
            close();
            loginWithOutLogout(user, 1);
        }
        SpiderLoginEventNode node = initLoginEventNode(user);
        boolean isSuccess = ChromDriverSpiderUtil.login(getWebDriver(), node);
        if(!isSuccess){
            ToolUtil.sleep(1000);
        }

        //确认是否已经登录成功
        if(!isLogin()){
            loginWithOutLogout(user, ++retryNum);
        }
    }

    @Override
    public boolean inputAndClick(String userId) {

        SpiderInputClickNode node = new SpiderInputClickNode();
        node.setClickXPath(Common.SINA_SEARCH_BUTTON);
        node.setContent(userId);
        node.setContentXPath(Common.SINA_SEARCH_INPUT);
        ChromDriverSpiderUtil.inputAndClick(getWebDriver(), node);
        return true;
    }

    @Override
    public boolean enterUserIndex(String userId) {

        SpiderMatchClickNode node = new SpiderMatchClickNode();
        node.setClickXPath(Common.SINA_RELATIVE_USER);
        node.setContentXPath(Common.SINA_RELATIVE_USER);
        node.setStratIndex(1);
        node.setEndIndex(4);
        node.setMatchContent(userId);
        ChromDriverSpiderUtil.matchAndClick(getWebDriver(), node);
        return true;
    }

    @Override
    public boolean like(Goal goal, User user, Node node) { //需要判断

        SpiderQueryContentNode queryNode = (SpiderQueryContentNode)node;
        boolean success = ChromDriverSpiderUtil.clickOrNot(getWebDriver(), "title", "赞", queryNode.getContentXPath());
        if(success) {
            goal.getMessageMetric().getLikeCount().increment();
            LOGGER.info("handler service like success {}-{}-{}", user.getUsername(), goal.getUserName(), goal.getId());
            return true;
        }

        LOGGER.info("handler service like nothing to do {}-{}-{}", user.getUsername(), goal.getUserName(), goal.getId());
        return false;
    }

    @Override
    public boolean share(Goal goal, User user, Node node) {

        SpiderInputClickNode inputClickNode = (SpiderInputClickNode)node;
        //1. 点击的地址
        ChromDriverSpiderUtil.click(getWebDriver(), inputClickNode.getTriggerXPath());
        boolean success = ChromDriverSpiderUtil.inputAndClick(getWebDriver(), inputClickNode);
        if(success) {
            goal.getMessageMetric().getShareCount().increment();
            LOGGER.info("handler service share success {}-{}-{}", user.getUsername(), goal.getUserName(), goal.getId());
            return true;
        }

        LOGGER.info("handler service share nothing to do {}-{}-{}", user.getUsername(), goal.getUserName(), goal.getId());
        return true;
    }

    @Override
    public boolean comment(Goal goal, User user, Node node) {

        SpiderInputClickNode inputClickNode = (SpiderInputClickNode)node;
        //1. 点击的地址
        ChromDriverSpiderUtil.click(getWebDriver(), inputClickNode.getTriggerXPath());
        boolean success = ChromDriverSpiderUtil.inputAndClick(getWebDriver(), inputClickNode);

        if(ChromDriverSpiderUtil.hasAlert(getWebDriver())){
            ChromDriverSpiderUtil.closeAlert(getWebDriver());
        }

        if(success) {
            goal.getMessageMetric().getCommentCount().increment();
            LOGGER.info("handler service comment success {}-{}-{}", user.getUsername(), goal.getUserName(), goal.getId());
            return true;
        }

        LOGGER.info("handler service comment nothing to do {}-{}-{}", user.getUsername(), goal.getUserName(), goal.getId());
        return true;
    }

    @Override
    public boolean collection(Goal goal, User user, Node node) {
        /*SpiderMatchClickNode node = new SpiderMatchClickNode();
        node.setClickXPath(Common.SINA_TOPIC_COLLECTION);
        node.setContentXPath(Common.SINA_TOPIC_TITLE);
        node.setStratIndex(2);
        node.setEndIndex(12);
        node.setMatchContent(goal.getMatchContent());*/
        ChromDriverSpiderUtil.matchAndClick(getWebDriver(), (SpiderMatchClickNode) node);
        return true;
    }

    @Override
    public boolean attention(Goal goal, User user, Node node) {

        SpiderQueryContentNode queryNode = (SpiderQueryContentNode)node;
        boolean success = ChromDriverSpiderUtil.click(getWebDriver(), queryNode.getContentXPath());

        if(success) {
            goal.getMessageMetric().getAttentionCount().increment();
            LOGGER.info("handler service attention success {}-{}-{}", user.getUsername(), goal.getUserName(), goal.getId());
            return true;
        }

        LOGGER.info("handler service attention nothing to do {}-{}-{}", user.getUsername(), goal.getUserName(), goal.getId());
        return false;
    }

    @Override
    public boolean logout(User user) {
        ChromDriverSpiderUtil.click(getWebDriver(), Common.SINA_LOGOUT_TOP);
        ChromDriverSpiderUtil.click(getWebDriver(), Common.SINA_LOGOUT);
        return true;
    }

    @Override
    public boolean isLogin() {

        try {
            String content = ChromDriverSpiderUtil.getContent(getWebDriver(), Common.SINA_LOGIN_ACK);
            if(StringUtils.isNotEmpty(content)){
                return true;
            }

            LOGGER.error("login fail");
        }catch (Exception e){
            LOGGER.error("login error", e);
        }
        return false;
    }

    @Override
    public String openUrlAndGetUser(Goal goal, User user, Node node) {

        for(int i=0; i<10; i++) {
            LOGGER.info("handler service open goal url  : {}-{}", goal.getId(), user.getUsername());
            MultiGoal multiGoal = (MultiGoal)goal;
            SpiderQueryContentNode queryNode = (SpiderQueryContentNode) node;
            ChromDriverSpiderUtil.openUrl(getWebDriver(), multiGoal.getUrl());
            String userName = ChromDriverSpiderUtil.getContent(getWebDriver(), queryNode.getContentXPath());
            if(StringUtils.isNotEmpty(userName)){
                LOGGER.info("handler service open goal url success  : {}-{}-{}", goal.getId(), user.getUsername(), userName);
                return userName;
            }
            try {
                Thread.sleep(1000*i);
            } catch (InterruptedException e) {
                LOGGER.error("login error", e);
            }
        }
        return null;
    }

    @Override
    public boolean fullAttention() {

        SpiderMatchClickNode node = new SpiderMatchClickNode();
        node.setClickXPath(Common.FULL_COMMENT_ATTENTION);
        node.setContentXPath(Common.FULL_COMMENT_ATTENTION);
        node.setMatchContent("关注");
        ChromDriverSpiderUtil.matchAndClick(getWebDriver(), node);
        LOGGER.info("关注成功");
        return true;
    }

    @Override
    public boolean fullLogin(User user, boolean isClick) {

        if(isClick) {
            ChromDriverSpiderUtil.click(getWebDriver(), Common.FULL_COMMENT_ATTENTION);
        }

        SpiderLoginEventNode node = fullInitLoginEventNode(user);
        ChromDriverSpiderUtil.login(getWebDriver(), node);

        //确认是否已经登录成功
        if(!isLogin()){
            fullLogin(user, false);
        }

        return true;
    }

    @Override
    public boolean fullLike() {

        ChromDriverSpiderUtil.clickOrNot(getWebDriver(), "title", "赞", Common.Full_LIKE);
        LOGGER.info("点赞成功");
        return true;
    }

    @Override
    public int getCommentCount(Node node) {
        SpiderSubElemNumNode subElemNumNode = new SpiderSubElemNumNode();
        return ChromDriverSpiderUtil.getSubElementCount(getWebDriver(), subElemNumNode.getParentXPath(), subElemNumNode.getTag());
    }

    @Override
    public String getCommentId(Node object) {

        SpiderQueryContentNode node = (SpiderQueryContentNode)object;
        return ChromDriverSpiderUtil.getContentWithKey(getWebDriver(), node.getContentXPath(), node.getKey());
    }

    @Override
    public void click(Node object) {
        SpiderQueryContentNode node = (SpiderQueryContentNode)object;
        ChromDriverSpiderUtil.click(getWebDriver(), node.getContentXPath());
    }

    @Override
    public void clickWithScollBottom(Node object) {
        ChromDriverSpiderUtil.scrollToBottom(getWebDriver());
        SpiderQueryContentNode node = (SpiderQueryContentNode)object;
        ChromDriverSpiderUtil.click(getWebDriver(), node.getContentXPath());
    }

    @Override
    public void close() {
        if(threadLocal.get()==null){
            return;
        }
        threadLocal.get().quit();
        threadLocal.remove();
    }

    private SpiderLoginEventNode fullInitLoginEventNode(User user) {

        if(user==null){
            throw new FansException("user is null");
        }

        SpiderLoginEventNode node = new SpiderLoginEventNode();
        node.setLoginXPath(Common.Full_LOGIN_BUTTON);
        node.setUserName(user.getUsername());
        node.setUserNameXPath(Common.FULL_LOGIN_USERNAME);
        node.setPasswd(user.getPwd());
        node.setPasswdXPath(Common.FULL_LOGIN_PWD);
        return node;
    }

    private SpiderLoginEventNode initLoginEventNode(User user) {

        if(user==null){
            throw new FansException("user is null");
        }

        SpiderLoginEventNode node = new SpiderLoginEventNode();
        node.setLoginURL(Common.SINA_URL);
        node.setLoginXPath(Common.SINA_LOGIN_BUTTON);
        node.setUserName(user.getUsername());
        node.setUserNameXPath(Common.SINA_LOGIN_USERNAME);
        node.setPasswd(user.getPwd());
        node.setPasswdXPath(Common.SINA_LOGIN_PWD);
        node.setValidateCodeXPath(Common.VALIDATE_CODE_IMAGE);
        return node;
    }

    public WebDriver getWebDriver(){
        try {
            WebDriver webDriver = threadLocal.get();
            if (webDriver == null) {
                webDriver = ChromDriverSpiderUtil.initDriver(0, 0);
                threadLocal.set(webDriver);
                Thread.sleep(1000);
            }
            return webDriver;
        }catch (Exception e){
            LOGGER.error("init webChrom fail", e);
        }

        throw new FansException("error");
    }
}