package com.rongduo.cashier.service;

import com.rongduo.cashier.mapper.OrderItemMapper;
import com.rongduo.cashier.mapper.OrderMapper;
import com.rongduo.cashier.mapper.ProductMapper;
import com.rongduo.cashier.model.order.Order;
import com.rongduo.cashier.model.order.OrderDetail;
import com.rongduo.cashier.model.order.OrderItem;
import com.rongduo.cashier.model.order.OrderStatus;
import com.rongduo.cashier.model.product.Product;
import com.rongduo.cashier.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

/**
 * @author: rongduo
 * @description: 商品购买的具体实现，购买，确认，取消
 * @date: 2022-09-07
 */
@Slf4j
@Service
public class OrderService {
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Autowired
    public OrderService(ProductMapper productMapper, OrderMapper orderMapper, OrderItemMapper orderItemMapper) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Transactional  // 整体上应该作为事务存储
    // toBoughtProductMap<商品 id, 要购买的数量>
    public Order create(User user, Map<Integer, Integer> toBoughtProductMap) {
        log.debug("toBoughtProductMap = {}", toBoughtProductMap);

        // 1. 确认商品的库存足够
        Map<Integer, Product> productMap = 确认商品库存是否足够(toBoughtProductMap);
        log.debug("productMap = {}", productMap);

        // 2. 商品减库存
        商品减库存(toBoughtProductMap);
        // 3. 创建订单记录
        Order order = 创建订单记录(user, productMap, toBoughtProductMap);
        // 4. 创建订单项记录
        插入订单项(order, productMap, toBoughtProductMap);

        return order;
    }

    private Order 创建订单记录(User user, Map<Integer, Product> productMap, Map<Integer, Integer> toBoughtProductMap) {
        String uuid = generateUUID();
        Timestamp createdAt = Timestamp.from(Instant.now());

        // 计算应付总额
        int payable = 计算应付总额(productMap, toBoughtProductMap);
        // 计算实付总额
        int actual = 计算实付总额(productMap, toBoughtProductMap);
        // 初识状态
        OrderStatus status = OrderStatus.未支付;

        Order order = new Order(user.getUserId(), uuid, createdAt, payable, actual, status);

        // OrderMapper 完成订单插入
        orderMapper.insert(order);
        log.debug("order = {}", order);

        return order;
    }

    private void 插入订单项(Order order, Map<Integer, Product> productMap, Map<Integer, Integer> toBoughtProductMap) {
        // 先遍历每个商品，构造 OrderItem 对象
        Set<Integer> productIdSet = productMap.keySet();
        Set<OrderItem> orderItemSet = new HashSet<>();
        for (Integer productId : productIdSet) {
            Product product = productMap.get(productId);
            int number = toBoughtProductMap.get(productId);

            OrderItem orderItem = new OrderItem(order, product, number);
            orderItemSet.add(orderItem);
        }

        orderItemMapper.insertBatch(orderItemSet);
    }

    // 实付总额就是每个商品的价格 * 商品购买数量 * 折扣，然后求和
    private int 计算实付总额(Map<Integer, Product> productMap, Map<Integer, Integer> toBoughtProductMap) {
        Set<Integer> productIdSet = productMap.keySet();

        int sum = 0;
        for (Integer productId : productIdSet) {
            int number = toBoughtProductMap.get(productId);
            Product product = productMap.get(productId);
            int price = product.getPrice();
            double discount = product.getDiscount() / 100.0;

            int productActual = (int)(price * number * discount);
            sum += productActual;
        }

        return sum;
    }

    // 实付总额就是每个商品的价格 * 商品购买数量，然后求和
    private int 计算应付总额(Map<Integer, Product> productMap, Map<Integer, Integer> toBoughtProductMap) {
        Set<Integer> productIdSet = productMap.keySet();

        int sum = 0;
        for (Integer productId : productIdSet) {
            int number = toBoughtProductMap.get(productId);
            Product product = productMap.get(productId);
            int price = product.getPrice();

            int productPayable = price * number;
            sum += productPayable;
        }

        return sum;
    }

    private String generateUUID() {
        String s = UUID.randomUUID().toString();
        return s.replace("-", "");
    }

    private void 商品减库存(Map<Integer, Integer> toBoughtProductMap) {
        for (Map.Entry<Integer, Integer> entry : toBoughtProductMap.entrySet()) {
            int productId = entry.getKey();
            int number = entry.getValue();

            productMapper.decrementStockByProductId(productId, number);
        }
    }

    private Map<Integer, Product> 确认商品库存是否足够(Map<Integer, Integer> toBoughtProductMap) {
        // 把所有的商品 id 收集起来
        Set<Integer> productIdSet = toBoughtProductMap.keySet();

        List<Product> productList = productMapper.selectProductListByProductIdSet(productIdSet);

        // 把 List<Product> -> Map<商品id, Product>
        Map<Integer, Product> productMap = new HashMap<>();
        for (Product product : productList) {
            productMap.put(product.getProductId(), product);
        }

        // TODO: 这里暂时不考虑传入的商品 id在表中不存在的情况了
        // 比较库存是否够
        for (Integer productId : productIdSet) {
            int number = toBoughtProductMap.get(productId);
            Product product = productMap.get(productId);

            if (number > product.getStock()) {
                throw new RuntimeException(product.getProductId().toString());   // 说明我们这个商品的库存是不够的
            }
        }

        // 说明所有商品的库存都是够的
        return productMap;
    }

    @Transactional  // 虽然只有一条 SQL，但加上也没错，本来一条 SQL 就是隐式事务
    public void confirm(int orderId) {
        // 更新订单记录（状态变成已支付，设置订单完成时间是当前时间）
        Timestamp finishedAt = Timestamp.from(Instant.now());
        OrderStatus status = OrderStatus.已支付;

        orderMapper.updateConfirm(orderId, finishedAt, status);
    }

    @Transactional
    public void cancel(int orderId) {
        // 1. 根据 orderId 查询出这个订单涉及的所有商品 id 和 它对应的购买数量
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderId(orderId);
        Map<Integer, Integer> toBoughtProductMap = new HashMap<>();
        for (OrderItem orderItem : orderItemList) {
            int productId = orderItem.getProductId();
            int number = orderItem.getProductNumber();
            toBoughtProductMap.put(productId, number);
        }

        // 2. 删除所有该 orderId 关联的 order_items 记录（删除订单项）
        orderItemMapper.deleteByOrderId(orderId);

        // 3. 删除该 orderId 对应的订单记录
        orderMapper.deleteByOrderId(orderId);

        // 4. 遍历每个商品，为每个商品增加库存
        for (Map.Entry<Integer, Integer> entry : toBoughtProductMap.entrySet()) {
            int productId = entry.getKey();
            int number = entry.getValue();

            productMapper.incrementStockByProductId(productId, number);
        }
    }

    public OrderDetail query(String uuid) {
        OrderDetail order = orderMapper.selectByUUID(uuid);

        order.setItemList(orderItemMapper.selectAllByOrderId(order.getOrderId()));

        return order;
    }

    public List<Order> getList() {
        return orderMapper.selectAll();
    }
}
