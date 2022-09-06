package com.rongduo.cashier.model.product;

import com.rongduo.cashier.controller.ErrorRedirectException;
import com.rongduo.cashier.model.AbsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: rongduo
 * @description: 商品单位验证逻辑
 * @date: 2022-09-02
 */
@Slf4j
@Component
public class UnitValidator extends AbsValidator {
    @Override
    public String validate(String module, String redirectUrl, String name) {
        name = super.validate(module, redirectUrl, name);

        if (name.length() > 10) {
            throw new ErrorRedirectException("value 的长度超过 10", module, redirectUrl);
        }

        return name;
    }
}
