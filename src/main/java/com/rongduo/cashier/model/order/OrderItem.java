package com.rongduo.cashier.model.order;

import com.rongduo.cashier.model.product.Product;
import lombok.Data;

/**
 * @author: rongduo
 * @description: 订单项实体类
 * @date: 2022-09-01
 */
@Data
public class OrderItem {
    private Integer id;
    private int orderId;
    private int productId;
    private String productName;
    private String productIntroduce;
    private int productNumber;
    private String productUnit;
    private int productPrice;
    private int productDiscount;

    public OrderItem() {}

    public OrderItem(Order order, Product product, int number) {
        this.orderId = order.getOrderId();
        this.productId = product.getProductId();
        this.productName = product.getName();
        this.productIntroduce = product.getIntroduce();
        this.productNumber = number;
        this.productUnit = product.getUnit();
        this.productPrice = product.getPrice();
        this.productDiscount = product.getDiscount();
    }
}
