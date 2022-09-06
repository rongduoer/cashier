package com.rongduo.cashier.model.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: rongduo
 * @description:用户实体类
 * @date: 2022-09-01
 */
@Slf4j
@Data
public class User {
    private Integer userId;
    private String username;
    private String password;

    //无参构造给Mybatis使用
    public User(){
        log.debug("model.user.User()");
    }
    public User(String username, String password) {
        log.debug("model.user.User(username = {}, password = {})",username,password);
        this.username = username;
        this.password = password;
    }
}
