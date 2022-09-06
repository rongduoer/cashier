package com.rongduo.cashier.model.product;

import com.rongduo.cashier.controller.ErrorRedirectException;
import com.rongduo.cashier.model.AbsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: rongduo
 * @description:商品名验证逻辑
 * @date: 2022-09-02
 */
@Slf4j
@Component
public class NameValidator extends AbsValidator {
    @Override
    public String validate(String module, String redirectUrl, String name) {
        name = super.validate(module, redirectUrl, name);

        if (name.length() > 100) {
            throw new ErrorRedirectException("value 的长度超过 100", module, redirectUrl);
        }

        return name;
    }
}