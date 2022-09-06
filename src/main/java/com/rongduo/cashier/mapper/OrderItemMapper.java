package com.rongduo.cashier.mapper;

import com.rongduo.cashier.model.order.OrderItem;
import com.rongduo.cashier.model.order.OrderItemDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@Mapper
public interface OrderItemMapper {
    void insertBatch(@Param("set") Set<OrderItem> orderItemSet);

    List<OrderItem> selectByOrderId(int orderId);

    void deleteByOrderId(int orderId);

    List<OrderItemDetail> selectAllByOrderId(int orderId);
}