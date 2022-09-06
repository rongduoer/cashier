package com.rongduo.cashier.mapper;

import com.rongduo.cashier.model.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    void insert(User user);

    // 由于只有一个参数，所以省略了 @Param 注解
    User selectByUsername(String username);
}
