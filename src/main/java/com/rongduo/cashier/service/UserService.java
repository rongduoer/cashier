package com.rongduo.cashier.service;

import com.rongduo.cashier.mapper.UserMapper;
import com.rongduo.cashier.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: rongduo
 * @description: 用户表的实现逻辑
 * @date: 2022-09-02
 */
@Slf4j
@Service
public class UserService {
    // 注入 UserMapper
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User register(String username, String password) {
        // 对密码进行加密处理
        String salt = BCrypt.gensalt();
        String hashPassword = BCrypt.hashpw(password, salt);

        // 进行插入操作
        User user = new User(username, hashPassword);
        userMapper.insert(user);

        return user;
    }

    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);

        log.debug("通过 mybatis 查询得到的用户 = {}", user);

        if (user == null) {
            return null;
        }

        // 进行密码验证
        if (!BCrypt.checkpw(password, user.getPassword())) {
            return null;
        }

        return user;
    }
}
