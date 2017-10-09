package com.my.controller;

import com.my.entity.Member;
import com.my.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Longe on 2017.09.27.
 */
@Controller
@RequestMapping(value = "member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "info")
    @ResponseBody
    public String getInfo(@RequestParam(value = "memMobile")String memMobile) {
        Member member = memberService.getMemberInfo(memMobile);
        return member.toString();
    }
}
