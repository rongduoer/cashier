package com.rongduo.cashier.model;

import com.rongduo.cashier.controller.ErrorRedirectException;

/**
 * @author: rongduo
 * @description:  发生错误的公共类
 * @date: 2022-09-02
 */

// 这个类不准备实例化对象，所以定义成抽象类
// 1. 不能是空的(null 或者 "")：所有
// 2. 长度不能超过多少：username <= 50   name <= 100   introduce <= 200   unit <= 10
// 3. 必须能转换成数字（stock 和 discount 是转成 int；price 转成 double）
// 4. 如果是数字，有一定的范围要求（stock 和 price > 0；discount (0, 100]
public abstract class AbsValidator {
    public String validate(String module, String redirectUrl, String value) {
        if (value == null) {
            throw new ErrorRedirectException("value 是 null", module, redirectUrl);
        }

        value = value.trim();
        if (value.isEmpty()) {
            throw new ErrorRedirectException("value 是 \"\"", module, redirectUrl);
        }

        return value;
    }
}
