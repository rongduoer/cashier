package com.rongduo.cashier.mapper;

import com.rongduo.cashier.model.order.Order;
import com.rongduo.cashier.model.order.OrderDetail;
import com.rongduo.cashier.model.order.OrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author: rongduo
 * @description:  订单表sql接口
 * @date: 2022-09-07
 */
@Repository
@Mapper
public interface OrderMapper {
    void insert(Order order);

    void updateConfirm(
            @Param("orderId") int orderId,
            @Param("finishedAt") Timestamp finishedAt,
            @Param("status") OrderStatus status);

    void deleteByOrderId(int orderId);

    OrderDetail selectByUUID(String uuid);

    List<Order> selectAll();
}