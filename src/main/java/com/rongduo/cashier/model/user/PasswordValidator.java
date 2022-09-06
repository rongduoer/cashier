package com.rongduo.cashier.model.user;

import com.rongduo.cashier.controller.ErrorRedirectException;
import com.rongduo.cashier.model.AbsValidator;
import com.rongduo.cashier.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author: rongduo
 * @description:
 * @date: 2022-09-02
 */
@Slf4j
@Component
public class PasswordValidator extends AbsValidator {
    // 由于除了非空的判断之外，没有其他判断逻辑了，所以暂时也不需要重写方法
}
