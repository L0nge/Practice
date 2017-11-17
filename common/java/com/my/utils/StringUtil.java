package com.my.utils;


import com.common.utils.config.GlobalConfig;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class StringUtil {

    /**
     * 判断是否为空为空，返回空字符串
     *
     * @param s
     * @return
     */
    public static String defaultString(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回AJAX操作成功的JSON字符串
     *
     * @param msg 返回信息
     * @return
     */
    public static String defaultSuccess(String msg) {
        StringBuffer sf = new StringBuffer();
        sf.append("{\"success\":true,\"msg\":\"").append(msg).append("\"}");
        return sf.toString();
    }

    /**
     * 返回AJAX操作失败的JSON字符串
     *
     * @param msg 返回信息
     * @return
     */
    public static String defaultFail(String msg) {
        StringBuffer sf = new StringBuffer();
        sf.append("{\"success\":false,\"msg\":\"").append(msg).append("\"}");
        return sf.toString();
    }

    /**
     * 判断字符串是否为NULL
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return str == null ? true : false;
    }

    /**
     * 判断字符串是否为空字符串(如果为空对象，先转换为空字符串)
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return "".equals(isNull(str) ? "" : str) ? true : false;
    }

    /**
     * 判断字符串是否为NULL或空字符串
     *
     * @param str
     * @return
     */
    public static boolean isNullAndEmpty(String str) {
        return (str == null || "".equals(str)) ? true : false;
    }

    /**
     * 判断传进来的字符串，是否 大于指定的字节，如果大于递归调用 直到小于指定字节数 ，一定要指定字符编码，因为各个系统字符编码都不一样，字节数也不一样
     *
     * @param s   原始字符串
     * @param num 传进来指定字节数
     * @return String 截取后的字符串
     */
    public static String idgui(String s, int num) throws Exception {
        int changdu = s.getBytes("UTF-8").length;
        if (changdu > num) {
            s = s.substring(0, s.length() - 1);
            s = idgui(s, num);
        }
        return s;
    }

    /**
     * 根据传过来的当前页，总记录数和结果集的JSON字符串拼装成对应的jqgrid格式的字符串
     *
     * @param page  当前页
     * @param total 总记录数
     * @param json  结果集的json字符串
     * @return
     */
    public static String getPageJson(int page, int total, String json) {
        return "{\"page\":\"" + page + "\",\"total\":\""
                + (total % GlobalConfig.pageSize == 0 ? total / GlobalConfig.pageSize
                : total / GlobalConfig.pageSize + 1)
                + "\",\"records\":\"" + total + "\",\"rows\":[" + json + "]}";
    }

    /**
     * 根据传过来的当前页，总记录数和结果集的JSON字符串拼装成对应的jqgrid格式的字符串,孙翔新增，不自带[....]符号
     *
     * @param page  当前页
     * @param total 总记录数
     * @param json  结果集的json字符串
     * @return
     */
    public static String getPageJsonNew(int page, int total, String json) {
        return "{\"page\":\"" + page + "\",\"total\":\""
                + (total % GlobalConfig.pageSize == 0 ? total / GlobalConfig.pageSize
                : total / GlobalConfig.pageSize + 1)
                + "\",\"records\":\"" + total + "\",\"rows\":" + json + "}";
    }

    /**
     * <h2>将字符串里的中文转换为UTF8</h2>
     *
     * @param input 输入字符串
     * @return
     */
    public static String stringToUTF8(String input) {
        StringBuffer output = new StringBuffer();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (Regex.isChinese(String.valueOf(ch))) {
                output.append("\\u" + Integer.toString(ch, 16));
            } else {
                output.append(ch);
            }
        }
        return output.toString();
    }

    /**
     * <h2>把一个字符串的第一个字母大写</h2>
     *
     * @param str 字符串
     * @return
     * @throws Exception
     */
    public static String capitalLetters(String str) {
        byte[] items = str.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    /**
     * 生成随机字符串
     *
     * @return
     */
    public static String create_nonce_str() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < 16; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    /**
     * 生成随机字符串
     *
     * @return
     */
    public static String create_nonce_code() {
        String chars = "0123456789";
        String res = "";
        for (int i = 0; i < 6; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    /**
     * 判断对象是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj.getClass().isArray()) {
            if (Array.getLength(obj) == 0) {
                return true;
            }
        }

        if (obj instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) obj;
            if (collection.isEmpty()) {
                return true;
            }
        }

        if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) obj;
            if (map.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static StringBuffer getRequestParam(HttpServletRequest request){
        StringBuffer requestParam=new StringBuffer();

        Map map = request.getParameterMap();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            String[] values = (String[]) map.get(key);
            for (String value : values) {
                if (requestParam.length()>0){
                    requestParam.append("&"+key+"="+value);
                }else {
                    requestParam.append("?"+key+"="+value);
                }
            }
        }
        return requestParam;
    }

}
