package com.rongduo.cashier;

import com.rongduo.cashier.model.user.User;
import com.rongduo.cashier.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.relational.core.sql.In;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class CashierApplicationTests {

    @Autowired
    private OrderService orderService;

    @Test
    void createOrder() {
        // 运行的前提是商品表里是 1 和 2 商品，并且库存分别 > 10 和 > 100
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 10);     // 3000 西瓜
        map.put(2, 100);   // 1000 大芝麻

        User user = new User("xxx", "yyy");
        user.setUserId(1);

        orderService.create(user, map);
    }

    @Test
    void confirmOrder() {
        // 运行的前提是，有一个 order_id 为 1 的订单存在，并且是未支付状态
        orderService.confirm(1);
    }

    @Test
    void cancelOrder() {
        // 运行的前提是，有一个 order_id 为 1 的订单存在，并且是未支付状态
        orderService.cancel(1);
    }

}
