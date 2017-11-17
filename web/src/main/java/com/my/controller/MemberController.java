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

/**
 * Created by Longe on 2017.09.27.
 */
@Controller
@RequestMapping(value = "member")
public class MemberController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "info")
    @ResponseBody
    public String getInfo(@RequestParam(value = "memMobile")String memMobile) {
        Member member = memberService.getMemberInfo(memMobile);
        LOGGER.info("{}", member);
        return member.toString();
    }
}
