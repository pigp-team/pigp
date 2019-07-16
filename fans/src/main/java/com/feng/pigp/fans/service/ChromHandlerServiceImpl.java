package com.feng.pigp.fans.service;

import com.feng.pigp.fans.common.Common;
import com.feng.pigp.fans.exception.AccountErrorException;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ChromHandlerServiceImpl.class);

    private static final int MAX_RETRY = 3;
    private static final String ERROR_TEXT = "异常";
    private static final String AGREE_TEXT = "同意";
    private static final String BAN_IMG = "不显示任何图片";
    private ThreadLocal<WebDriver> threadLocal = new ThreadLocal<WebDriver>();

    @Override
    public boolean login(User user) {

        LOGGER.info("handler service login start {}", user.getUsername());
        if(threadLocal.get()!=null){
            logout(user);
        }

        banLoadImage(false);//设置可以加载图片
        loginWithOutLogout(user,null, 1);
        LOGGER.info("handler service login success {}", user.getUsername());
        banLoadImage(true);//禁用图片
        return true;
    }

    public void banLoadImage(boolean banImage){

        ChromDriverSpiderUtil.openUrl(getWebDriver(), Common.SETTING_IMAGE_URL, Common.SETTING_IMAGE_TXT);
        String content = ChromDriverSpiderUtil.getContent(getWebDriver(), Common.SETTING_IMAGE_TXT);
        if(BAN_IMG.equals(content) && !banImage){//目前是禁用
            ChromDriverSpiderUtil.click(getWebDriver(), Common.SETTING_IMAGE_TXT);
        }

        if(!BAN_IMG.equals(content) && banImage){
            ChromDriverSpiderUtil.click(getWebDriver(), Common.SETTING_IMAGE_TXT);
        }
    }

    private boolean login(User user, String url) {

        LOGGER.info("handler service login start {}", user.getUsername());
        if(threadLocal.get()!=null){
            logout(user);
        }

        loginWithOutLogout(user, url, 1);
        LOGGER.info("handler service login success {}", user.getUsername());
        return true;
    }

    private void loginWithOutLogout(User user, String openUrl, int retryNum) {

        if(retryNum>=MAX_RETRY){
            //close();
            loginWithOutLogout(user, openUrl, 1);
        }
        SpiderLoginEventNode node = initLoginEventNode(user, openUrl);
        //ChromDriverSpiderUtil.click(getWebDriver(), Common.INTERNALE_SINA_LOGIN_URL);
        //ChromDriverSpiderUtil.wait(getWebDriver(), node.getUserNameXPath());
        boolean isSuccess = ChromDriverSpiderUtil.login(getWebDriver(), node);
        if(!isSuccess){
            loginWithOutLogout(user, openUrl, ++retryNum);
        }

        if(isErrorUser()){
            LOGGER.error("account err : {}", user.getUsername());
            throw new AccountErrorException("account user error");
        }
        //确认是否已经登录成功
        if(!isLogin(user)){
            loginWithOutLogout(user,openUrl, ++retryNum);
        }
    }

    private boolean isErrorUser() {

        String content = ChromDriverSpiderUtil.getContent(getWebDriver(), Common.ACCOUNT_ALL_ERROR);
        if(StringUtils.isEmpty(content)){
            return false;

        }

        LOGGER.error("err message : {}", content);
        if(content.contains(ERROR_TEXT)){
            return true;
        }

        return true;
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
            user.getMessageMetric().getLikeCount().increment();
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
            user.getMessageMetric().getShareCount().increment();
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
            user.getMessageMetric().getCommentCount().increment();
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

        //同意协议
        if(AGREE_TEXT.equals(ChromDriverSpiderUtil.getContent(getWebDriver(), Common.AGREE_RULE))){
            LOGGER.info("user agree protol : {}", user.getUsername());
            ChromDriverSpiderUtil.click(getWebDriver(), Common.AGREE_RULE);
        }

        SpiderQueryContentNode queryNode = (SpiderQueryContentNode)node;
        boolean success = ChromDriverSpiderUtil.click(getWebDriver(), queryNode.getContentXPath());

        //判断是否为异常账号：
        if(ERROR_TEXT.equals(ChromDriverSpiderUtil.getContentWithKey(getWebDriver(), Common.ATTENTION_ALERT_ERROR,null))){
            LOGGER.error("account unnormal :{}", user.getUsername());
            //点击取消
            ChromDriverSpiderUtil.click(getWebDriver(), Common.ATTENTION_ALERT_CLOSE);
            throw new AccountErrorException("account error");
        }

        if(success) {
            user.getMessageMetric().getAttentionCount().increment();
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
    public boolean isLogin(User user) {

        try {
            String content = ChromDriverSpiderUtil.getContent(getWebDriver(), Common.SINA_LOGIN_ACK);
            if(StringUtils.isNotEmpty(content)){
                LOGGER.info("user login success :{}-{}", user.getUsername(), content);
                return true;
            }

            LOGGER.error("login fail");
        }catch (Exception e){
            LOGGER.error("login error", e);
        }
        return false;
    }

    @Override
    public boolean openNewWindows(User user, String url) {
        ChromDriverSpiderUtil.openNewWindows(getWebDriver(), url);
        return true;
    }

    @Override
    public String openUrlAndGetUser(Goal goal, User user, Node node) {

        try {

            LOGGER.info("handler service open goal url  : {}-{}", goal.getId(), user.getUsername());
            MultiGoal multiGoal = (MultiGoal) goal;
            SpiderQueryContentNode queryNode = (SpiderQueryContentNode) node;
            ChromDriverSpiderUtil.openUrl(getWebDriver(), multiGoal.getUrl(), queryNode.getContentXPath());
            //如果页面加载失败，下一句操作会堵住
            String userName = ChromDriverSpiderUtil.getContent(getWebDriver(), queryNode.getContentXPath());

            if (StringUtils.isNotEmpty(userName)) {
                LOGGER.info("handler service open goal url success  : {}-{}-{}", goal.getId(), user.getUsername(), userName);
                return userName;
            }

            ChromDriverSpiderUtil.refresh(getWebDriver());

        }catch (Exception e){
            LOGGER.debug("open url and get error", e);
        }
        return null;
    }

    @Override
    public int getCommentCount(Node node) {
        SpiderSubElemNumNode subElemNumNode = new SpiderSubElemNumNode();
        return ChromDriverSpiderUtil.getSubElementCount(getWebDriver(), subElemNumNode.getParentXPath(), subElemNumNode.getTag());
    }

    @Override
    public String getContent(Node object) {

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

    @Override
    public void refresh() {
        ChromDriverSpiderUtil.refresh(getWebDriver());
    }

    @Override
    public void switchWindowns() {
        ChromDriverSpiderUtil.switchOtherWindows(getWebDriver());
    }

    private SpiderLoginEventNode initLoginEventNode(User user, String openUrl) {

        if(user==null){
            throw new FansException("user is null");
        }

        SpiderLoginEventNode node = new SpiderLoginEventNode();
        node.setLoginURL(Common.INTERNALE_SINA_LOGIN_URL);
        if(StringUtils.isNotEmpty(openUrl)) {
            node.setLoginURL(openUrl);
        }
        node.setLoginXPath(Common.INTERNALE_SINA_LOGIN_BUTTON);
        node.setUserName(user.getUsername());
        node.setUserNameXPath(Common.INTERNALE_SINA_LOGIN_USERNAME);
        node.setPasswd(user.getPwd());
        node.setPasswdXPath(Common.INTERNALE_SINA_LOGIN_PWD);
        node.setValidateCodeXPath(Common.INTERNALE_VALIDATE_CODE_IMAGE);
        node.setValidateCodeInputXPath(Common.INTERNALE_VALIDATE_CODE_INPUT);
        return node;
    }

    public WebDriver getWebDriver(){
        try {
            WebDriver webDriver = threadLocal.get();
            if (webDriver == null) {
                webDriver = ChromDriverSpiderUtil.initDriver(0, 0);
                ChromDriverSpiderUtil.openNewWindows(webDriver, Common.M_SINA_URL);
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