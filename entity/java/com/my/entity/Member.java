package com.my.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Longe on 2017.09.29.
 */
@Getter
@Setter
@ToString
public class Member {
    /**
     * 用户名
     */
    private String memMobile;
    /**
     * 密码
     */
    private String memPwd;
}
