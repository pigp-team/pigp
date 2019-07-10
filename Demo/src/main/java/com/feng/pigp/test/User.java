package com.feng.pigp.test;

/**
 * @author feng
 * @date 2019/6/25 12:55
 * @since 1.0
 */
public class User {

    private String userName;
    private int age;

    public User(String userName, int age) {
        System.out.println("enter User");
        this.userName = userName;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", age=" + age +
                '}';
    }
}