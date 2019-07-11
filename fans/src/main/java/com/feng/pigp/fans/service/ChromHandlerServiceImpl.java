package com.feng.pigp.fans.service;

import com.feng.pigp.fans.common.Common;
import com.feng.pigp.fans.exception.FansException;
import com.feng.pigp.fans.model.Goal;
import com.feng.pigp.fans.model.User;
import com.feng.pigp.fans.model.chrom.SpiderInputClickNode;
import com.feng.pigp.fans.model.chrom.SpiderLoginEventNode;
import com.feng.pigp.fans.model.chrom.SpiderMatchClickNode;
import com.feng.pigp.fans.util.ChromDriverSpiderUtil;
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
public class ChromHandlerServiceImpl implements HandlerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChromHandlerServiceImpl.class);

    private ThreadLocal<WebDriver> threadLocal = new ThreadLocal<WebDriver>();

    @Override
    public boolean login(User user) {

        if(threadLocal.get()!=null){
            logout();
        }

        SpiderLoginEventNode node = initLoginEventNode(user);
        ChromDriverSpiderUtil.login(getWebDriver(), node);

        //确认是否已经登录成功
        try {
            String content = ChromDriverSpiderUtil.getContent(getWebDriver(), Common.SINA_LOGIN_ACK);
            if(content!=null && content.contains("用户")){
                return true;
            }

            LOGGER.error("login fail");
            login(user);

        }catch (Exception e){
            LOGGER.error("login error", e);
            login(user);
        }

        return false;
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
    public boolean like(Goal goal) { //需要判断
        SpiderMatchClickNode node = new SpiderMatchClickNode();
        node.setClickXPath(Common.SINA_TOPIC_LIKE);
        node.setContentXPath(Common.SINA_TOPIC_TITLE);
        node.setClickContent("赞");
        node.setClickKey("title");
        node.setStratIndex(2);
        node.setEndIndex(12);
        node.setMatchContent(goal.getMatchContent());
        ChromDriverSpiderUtil.matchAndClick(getWebDriver(), node);
        return true;
    }

    @Override
    public boolean share(Goal goal) {

        SpiderMatchClickNode node = new SpiderMatchClickNode();
        node.setClickXPath(Common.SINA_TOPIC_SHARE);
        node.setContentXPath(Common.SINA_TOPIC_TITLE);
        node.setStratIndex(2);
        node.setEndIndex(12);
        node.setMatchContent(goal.getMatchContent());
        ChromDriverSpiderUtil.matchAndClick(getWebDriver(), node);

        SpiderInputClickNode inputClickNode = new SpiderInputClickNode();
        inputClickNode.setContent("xxx");
        inputClickNode.setContentXPath(Common.SINA_TOPIC_SHARE_INPUT);
        inputClickNode.setClickXPath(Common.SINA_TOPIC_SHARE_BUTTON);
        ChromDriverSpiderUtil.inputAndClick(getWebDriver(), inputClickNode);
        return true;
    }

    @Override
    public boolean comment(Goal goal) {

        SpiderMatchClickNode node = new SpiderMatchClickNode();
        node.setClickXPath(Common.SINA_TOPIC_COMMENT);
        node.setContentXPath(Common.SINA_TOPIC_TITLE);
        node.setStratIndex(2);
        node.setEndIndex(12);
        node.setMatchContent(goal.getMatchContent());
        int index = ChromDriverSpiderUtil.matchAndClick(getWebDriver(), node);

        if(index==-1){
            LOGGER.error("not find topic : {}", GsonUtil.toJson(goal));
            return false;
        }

        SpiderInputClickNode inputClickNode = new SpiderInputClickNode();
        inputClickNode.setContent("xxx");
        inputClickNode.setContentXPath(String.format(Common.SINA_TOPIC_COMMENT_INPUT, index));
        inputClickNode.setClickXPath(String.format(Common.SINA_TOPIC_COMMENT_BUTTON, index));
        ChromDriverSpiderUtil.inputAndClick(getWebDriver(), inputClickNode);
        return true;
    }

    @Override
    public boolean collection(Goal goal) {
        SpiderMatchClickNode node = new SpiderMatchClickNode();
        node.setClickXPath(Common.SINA_TOPIC_COLLECTION);
        node.setContentXPath(Common.SINA_TOPIC_TITLE);
        node.setStratIndex(2);
        node.setEndIndex(12);
        node.setMatchContent(goal.getMatchContent());
        ChromDriverSpiderUtil.matchAndClick(getWebDriver(), node);
        return true;
    }

    @Override
    public boolean attention(Goal goal) {
        ChromDriverSpiderUtil.click(getWebDriver(), Common.SINA_ATTENTION);
        return true;
    }

    @Override
    public boolean logout() {
        ChromDriverSpiderUtil.click(getWebDriver(), Common.SINA_LOGOUT_TOP);
        ChromDriverSpiderUtil.click(getWebDriver(), Common.SINA_LOGOUT);
        return true;
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
        return node;
    }

    public WebDriver getWebDriver(){
        WebDriver webDriver = threadLocal.get();
        if(webDriver==null){
            webDriver = ChromDriverSpiderUtil.initDriver(0, 0);
            threadLocal.set(webDriver);
        }
        return webDriver;
    }
}