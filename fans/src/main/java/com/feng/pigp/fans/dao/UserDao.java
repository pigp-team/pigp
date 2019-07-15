package com.feng.pigp.fans.dao;

import com.feng.pigp.fans.model.User;

import java.util.List;

/**
 * @author feng
 * @date 2019/7/15 16:28
 * @since 1.0
 */
public interface UserDao {

    int insert(User user);
    int batchInsert(List<User> userList);
    int delete(int id);
    int batchDelete(List<Integer> idList);
    User query(User user);
    List<User> batchQuery(List<User> userList);
    int update(User user);
}