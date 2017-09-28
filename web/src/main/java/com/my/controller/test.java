package com.my.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Longe on 2017.09.27.
 */
@Controller
@RequestMapping(value = "/test")
public class test {
    @RequestMapping(value = "/hello")
    @ResponseBody
    public String hello(@RequestParam(value = "name")String name) {
        return "hello" + name;
    }
}
