package com.feng.pigp.fans.model.chrom;

/**
 * @author feng
 * @date 2019/3/11 16:43
 * @since 1.0
 */
public class SpiderLoginEventNode{

    private String userName;
    private String userNameXPath;
    private String passwd;
    private String passwdXPath;
    private String loginURL;
    private String loginXPath;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNameXPath() {
        return userNameXPath;
    }

    public void setUserNameXPath(String userNameXPath) {
        this.userNameXPath = userNameXPath;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPasswdXPath() {
        return passwdXPath;
    }

    public void setPasswdXPath(String passwdXPath) {
        this.passwdXPath = passwdXPath;
    }

    public String getLoginURL() {
        return loginURL;
    }

    public void setLoginURL(String loginURL) {
        this.loginURL = loginURL;
    }

    public String getLoginXPath() {
        return loginXPath;
    }

    public void setLoginXPath(String loginXPath) {
        this.loginXPath = loginXPath;
    }
}