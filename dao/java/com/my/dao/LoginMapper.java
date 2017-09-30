package com.my.dao;

import com.my.entity.Member;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Longe on 2017.09.29.
 */
public interface LoginMapper {
    //查询会员信息
    Member getMemberInfo(@Param(value = "memMobile")String memMobile);
}
