package com.my.controller;

import com.my.entity.Member;
import com.my.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by Longe on 2017.09.27.
 */
@Controller
@RequestMapping(value = "member")
public class MemberController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

    @Resource
    private MemberService memberService;

    @RequestMapping(value = "info")
    @ResponseBody
    public String getInfo(@RequestParam(value = "mobile")String mobile) {
        Member member = memberService.getMemberInfo(mobile);
        LOGGER.info("{}", member);
        return member.toString();
    }

    @RequestMapping(value = "add")
    @ResponseBody
    public String addMember(@RequestParam(value = "mobile")String mobile,
                            @RequestParam(value = "pwd")String pwd) {
        Member member = new Member();
        member.setMemMobile(mobile);
        member.setMemPwd(pwd);
        this.memberService.addMember(member);
        return "success";
    }
}
