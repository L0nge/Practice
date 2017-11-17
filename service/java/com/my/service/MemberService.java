package com.my.service;

import com.my.entity.Member;

/**
 * Created by Longe on 2017.09.30.
 */
public interface MemberService {
    //查询会员信息
    Member getMemberInfo(String memMobile);

    Integer addMember(Member member);
}
