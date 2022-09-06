package com.rongduo.cashier.model.product;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: rongduo
 * @description: 这个对象只是承载请求参数的，属性名和 form 表单中 input 的 name 一致
 * @date: 2022-09-02
 */
@Slf4j
// 这个对象只是承载请求参数的，要求属性的名称必须和 form 表单中的 input 的 name 一致
@Data
public class ProductParam {
    private String productId;
    private String name;
    private String introduce;
    private String stock;
    private String unit;
    private String price;
    private String discount;

    public ProductParam() {
        log.debug("model.product.ProductParam()");
    }

    public void setName(String name) {
        log.debug("model.product.ProductParam.setName(name = {})", name);
        this.name = name;
    }

    public void setIntroduce(String introduce) {
        log.debug("model.product.ProductParam.setIntroduce(introduce = {})", introduce);
        this.introduce = introduce;
    }

    public void setStock(String stock) {
        log.debug("model.product.ProductParam.setStock(stock = {})", stock);
        this.stock = stock;
    }

    public void setUnit(String unit) {
        log.debug("model.product.ProductParam.setUnit(unit = {})", unit);
        this.unit = unit;
    }

    public void setPrice(String price) {
        log.debug("model.product.ProductParam.setPrice(price = {})", price);
        this.price = price;
    }

    public void setDiscount(String discount) {
        log.debug("model.product.ProductParam.setDiscount(discount = {})", discount);
        this.discount = discount;
    }

    public void validate(String module, String redirectUrl) {

    }
}
