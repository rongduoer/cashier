package com.rongduo.cashier.model.user;

import com.rongduo.cashier.controller.ErrorRedirectException;
import com.rongduo.cashier.model.AbsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: rongduo
 * @description: 用户名验证类
 * @date: 2022-09-02
 */
@Slf4j
@Component  // 交给 Spring 管理
public class UsernameValidator extends AbsValidator {
    public String validate(String module, String redirectUrl, String username) {
        username = super.validate(module, redirectUrl, username);

        if (username.length() > 50) {
            throw new ErrorRedirectException("username 的长度超过 50", module, redirectUrl);
        }

        return username;
    }
}
