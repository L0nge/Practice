package com.my.service.impl;

import com.my.dao.LoginMapper;
import com.my.entity.Member;
import com.my.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public Member getMemberInfo(String memMobile) {
        return loginMapper.getMemberInfo(memMobile);
    }
}
