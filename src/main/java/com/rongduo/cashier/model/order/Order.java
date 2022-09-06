package com.rongduo.cashier.model.order;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author: rongduo
 * @description: 订单实体类
 * @date: 2022-09-01
 */
@Data
public class Order {
    private Integer orderId;
    private int userId;
    private String uuid;
    private Timestamp createdAt;
    private Timestamp finishedAt;
    private int payable;        // 应付金额
    private int actual;         // 实付金额
    private OrderStatus status; // 订单状态

    public Order() {}

    public Order(Integer userId, String uuid, Timestamp createdAt, int payable, int actual, OrderStatus status) {
        this.userId = userId;
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.payable = payable;
        this.actual = actual;
        this.status = status;
    }

    public void setStatus(int status) {
        if (status == 1) {
            this.status = OrderStatus.未支付;
        } else {
            this.status = OrderStatus.已支付;
        }
    }
}
