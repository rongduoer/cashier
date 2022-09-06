package com.rongduo.cashier.model.product;

import com.rongduo.cashier.model.user.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: rongduo
 * @description:商品实体类
 * @date: 2022-09-01
 */
@Slf4j
@Data
public class Product {
    // (#{name}, #{introduce}, #{stock}, #{unit}, #{price}, #{discount})
    private Integer productId;
    private int userId;     // 上架的用户是哪个
    private String name;
    private String introduce;
    private int stock;
    private String unit;
    private int price;  // 数据库中保存的单位是'分',也就是用户传入的价格 * 100 的结果，所以不会有小数，整型即可
    private int discount;

    // 同样的准备好一个无参构造方法，给 Mybatis select 的时候使用
    public Product() {
        log.debug("model.product.Product()");
    }

    public Product(User user, ProductParam param) {
        log.debug("model.product.Product(user = {}, param = {})", user, param);

        this.productId = param.getProductId() != null ? Integer.parseInt(param.getProductId()) : null;

        this.userId = user.getUserId();
        this.name = param.getName();
        this.introduce = param.getIntroduce();
        this.stock = Integer.parseInt(param.getStock());    // 如果大家传的 stock 不合法（不是整型），这一步可能抛异常
        this.unit = param.getUnit();
        this.price = (int) (Double.parseDouble(param.getPrice()) * 100); // 1）可能抛异常；2）如果传入的价格参数有小数点后二位之后，则直接丢弃
        this.discount = Integer.parseInt(param.getDiscount());  // 也可能抛异常
    }

    public Integer getProductId() {
        log.debug("model.product.Product.getProductId()");
        return productId;
    }

    public int getUserId() {
        log.debug("model.product.Product.getUserId()");
        return userId;
    }

    public String getName() {
        log.debug("model.product.Product.getName()");
        return name;
    }

    public String getIntroduce() {
        log.debug("model.product.Product.getIntroduce()");
        return introduce;
    }

    public int getStock() {
        log.debug("model.product.Product.getStock()");
        return stock;
    }

    public String getUnit() {
        log.debug("model.product.Product.getUnit()");
        return unit;
    }

    public int getPrice() {
        log.debug("model.product.Product.getPrice()");
        return price;
    }

    public int getDiscount() {
        log.debug("model.product.Product.getDiscount()");
        return discount;
    }

    public void setProductId(Integer productId) {
        log.debug("model.product.Product.setProductId(productId = {})", productId);
        this.productId = productId;
    }

    public void setUserId(int userId) {
        log.debug("model.product.Product.setUserId(userId = {})", userId);
        this.userId = userId;
    }

    public void setName(String name) {
        log.debug("model.product.Product.setName(name = {})", name);
        this.name = name;
    }

    public void setIntroduce(String introduce) {
        log.debug("model.product.Product.setIntroduce(introduce = {})", introduce);
        this.introduce = introduce;
    }

    public void setStock(int stock) {
        log.debug("model.product.Product.setStock(stock = {})", stock);
        this.stock = stock;
    }

    public void setUnit(String unit) {
        log.debug("model.product.Product.setUnit(unit = {})", unit);
        this.unit = unit;
    }

    public void setPrice(int price) {
        log.debug("model.product.Product.setPrice(price = {})", price);
        this.price = price;
    }

    public void setDiscount(int discount) {
        log.debug("model.product.Product.setDiscount(discount = {})", discount);
        this.discount = discount;
    }
}
