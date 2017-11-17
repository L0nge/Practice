package com.my.service.impl;

import com.my.dao.LoginMapper;
import com.my.entity.Member;
import com.my.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class MemberServiceImpl implements MemberService {
    @Resource
    private LoginMapper loginMapper;

    @Override
    public Member getMemberInfo(String memMobile) {
        return loginMapper.getMemberInfo(memMobile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addMember(Member member) {
        member.setMemPwd("123");
        member.setMemMobile("123");
        this.loginMapper.addMember(member);
        this.loginMapper.addMember(new Member());
        return null;
    }
}
