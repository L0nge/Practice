package com.my.utils.config;

public class GlobalConfig {

	/**
	 * 每页分页数，全局值
	 */
	public static int pageSize = 12;
	
	/**
	 * Session保存的用户对象KEY
	 */
	public static String sessionUser = "currentUser";
	
	/**
	 * Session保存的角色对象KEY
	 */
	public static String sessionRole = "role";
	
	
	/**
	 * Session保存的角色功能对象KEY
	 */
	public static String sessionPage = "pages";
	

    /**
     * 图片服务器地址
     */
    //public static final String PIC_SERVER_IP = "10.101.31.120";
    
    /**
     * 正式图片服务器地址
     */
    public static final String PIC_SERVER_IP = "h5.joypay.cn";
    
    /**
     * 图片服务器用户名
     */
    //public static final String PIC_SERVER_USER = "image_test";
    
    /**
     * 正式图片服务器用户名
     */
    public static final String PIC_SERVER_USER = "web_image";
   

    /**
     * 图片服务器密码
     */
    //public static final String PIC_SERVER_PWD = "Mag&Tv_img0";
    
    /**
     * 图片服务器密码
     */
    public static final String PIC_SERVER_PWD = "Img-R&W9";
    
    /**
     * 图片服务器存放广告信息位置
     */
    public static final String PIC_SERVER_AD = "ecmallad";

    /**
     * 图片服务器存放商品信息位置
     */
    public static final String PIC_SERVER_PRODUCT = "ecmallproduct";

    /**
     * 图片服务器端口
     */
    public static final int PIC_SERVER_PROT = 21;




}
