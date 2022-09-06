package com.rongduo.cashier.model.order;

/**
 * @author: rongduo
 * @description: 使用枚举类型定义订单状态
 * @date: 2022-09-07
 */
public enum OrderStatus {
    未支付(1), 已支付(2);

    private final int value;

    // 未支付(1)
    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
