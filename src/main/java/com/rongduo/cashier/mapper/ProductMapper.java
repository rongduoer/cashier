package com.rongduo.cashier.mapper;

import com.rongduo.cashier.model.product.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@Mapper
public interface ProductMapper {
    void insert(Product product);

    List<Product> selectAll();

    void updateByProductId(Product product);

    List<Product> selectProductListByProductIdSet(@Param("list") Set<Integer> productIdSet);

    void decrementStockByProductId(@Param("productId") int productId, @Param("number") int number);

    void incrementStockByProductId(@Param("productId") int productId, @Param("number") int number);
}
