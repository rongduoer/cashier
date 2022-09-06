package com.rongduo.cashier.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
// 添加这个注解之后，这个类会被作为异常统一处理的类 // AOP 思想的体现（AfterResourceHandler）
// AOP 里 Advice 是通知的意思
@ControllerAdvice
public class CashControllerAdvice {
    public CashControllerAdvice() {
        log.debug("controller.CashControllerAdvice()");
    }

    // 关于错误处理的内容，单独扔到一个地方去
    @ExceptionHandler(ErrorRedirectException.class)
    public String logAndRedirect(ErrorRedirectException exc) {
        // 根据不同的逻辑，打印不同，跳转的页面也不同
        log.debug("{}: {}", exc.getModule(), exc.getError());
        return "redirect:" + exc.getRedirectUrl();
    }
}
