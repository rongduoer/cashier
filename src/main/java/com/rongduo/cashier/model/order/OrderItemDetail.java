package com.rongduo.cashier.model.order;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: rongduo
 * @description:
 * @date: 2022-09-07
 */
@Setter
@ToString
@EqualsAndHashCode
public class OrderItemDetail {
    private int productId;
    private String productName;
    private int productPrice;
    private int productNumber;
    private String productUnit;
    private int productDiscount;

    public int getId() {
        return productId;
    }

    public String getName() {
        return productName;
    }

    public double getPrice() {
        return productPrice / 100.0;
    }

    public int getNumber() {
        return productNumber;
    }

    public String getUnit() {
        return productUnit;
    }

    public double getPayable() {
        return getPrice() * productNumber;
    }

    public double getActual() {
        return getPayable() * productDiscount / 100.0;
    }
}

