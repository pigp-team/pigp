package com.feng.pigp.fans.service;

import com.feng.pigp.fans.common.Common;
import com.feng.pigp.fans.exception.FansException;
import com.feng.pigp.fans.model.User;
import com.feng.pigp.fans.model.chrom.SpiderInputClickNode;
import com.feng.pigp.fans.model.chrom.SpiderLoginEventNode;
import com.feng.pigp.fans.util.ChromDriverSpiderUtil;
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

        SpiderLoginEventNode node = initLoginEventNode(user);
        WebDriver webDriver = getWebDriver();
        try {
            ChromDriverSpiderUtil.login(webDriver, node);
        } catch (InterruptedException e) {
            LOGGER.error("login error", e);
        }
        return true;
    }

    @Override
    public boolean inputAndClick(String userId) {

        SpiderInputClickNode node = new SpiderInputClickNode();
        node.setClickXPath(Common.SINA_SEARCH_BUTTON);
        node.setContent(userId);
        node.setContentXPath(Common.SINA_SEARCH_BUTTON);
        ChromDriverSpiderUtil.inputAndClick(getWebDriver(), node);
        return true;
    }

    @Override
    public boolean enterUserIndex(String userId) {

        for(int i=1; i<=4; i++){
            if(userId.equals(ChromDriverSpiderUtil.getContent(getWebDriver(), String.format(Common.SINA_RELATIVE_USER, i)))){
                ChromDriverSpiderUtil.click(getWebDriver(), String.format(Common.SINA_RELATIVE_USER, i));
            }
        }

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