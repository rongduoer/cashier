package com.rongduo.cashier.service;

import com.rongduo.cashier.mapper.ProductMapper;
import com.rongduo.cashier.model.product.Product;
import com.rongduo.cashier.model.product.ProductParam;
import com.rongduo.cashier.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: rongduo
 * @description: 商品服务类，具体的实现
 * @date: 2022-09-02
 */
@Slf4j
@Service
public class ProductService {
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Product create(User user, ProductParam param) {
        // 1. 构造一个 Product 对象（从 ProductParam 对象）
        Product product = new Product(user, param);
        // 2. 调用 mapper 的 insert，完成数据库插入
        productMapper.insert(product);
        // 3. 返回构造好的对象
        return product;
    }

    public List<Product> getList() {
        return productMapper.selectAll();
    }

    public Product update(User user, ProductParam productParam) {
        Product product = new Product(user, productParam);

        productMapper.updateByProductId(product);

        return product;
    }
}
