package com.my.utils.sms;

import com.my.utils.httpClient.HttpClientUtil;
import com.my.utils.encode.MD5;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SendMsg {

    private static final Log logger = LogFactory.getLog(SendMsg.class);
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    /**
     * 发送短信接口方法
     *
     * @param phone：电话号码（多个号码间以逗号隔开）,但最多不超过50个
     * @param content：                         发送短信内容
     * @return
     */
    public static String sendMess(String phone, String content) {


        //获取时间戳
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = sdf.format(date);

        //获取mhtMsgIds值
        String mhtMsgIds = "20150923120000001";
        String[] arry;
        arry = phone.split(",");
        Long b = Long.parseLong(mhtMsgIds);
        String st = "";
        for (int i = 0; i < arry.length && i < 50; i++) {
            mhtMsgIds = (b + i) + "";
            st = st + mhtMsgIds + ",";
            mhtMsgIds = st.substring(0, st.length() - 1);
        }

        String resultCode = "";
        // 创建StringBuffer对象用来保存接口地址
        StringBuffer sb = new StringBuffer(ConstReference.SMS_URL);
        // 创建StringBuffer对象用来发送参数
        StringBuffer mess = new StringBuffer();

        // 向StringBuffer追加用户名
        mess.append("userName=").append(ConstReference.USER_NAME);
        // 向StringBuffer追加时间戳
        mess.append("&timestamp=").append(str);
        // 向StringBuffer追加密码（密码采用MD5 32位 小写）
        mess.append("&userPassword=").append(ConstReference.USER_PASSWORD);
        // 向StringBuffer追加签名
        mess.append("&sign=").append(MD5.string2MD5(ConstReference.USER_PASSWORD + str).toUpperCase());
        // 向StringBuffer追加服务码
        mess.append("&serviceCode=").append(ConstReference.SERVICE_CODE);
        // 向StringBuffer追加手机号码
        mess.append("&phones=").append(phone);

        mess.append("&mhtMsgIds=").append(mhtMsgIds);
        mess.append("&sendTime=").append("");
        mess.append("&priority=").append("5");
        mess.append("&orgCode=").append("");
        mess.append("&msgType=").append("1");
        mess.append("&reportFlag=").append("0");
        // 向StringBuffer追加消息内容
        mess.append("&msgContent=").append(content);

        try {
            // 发送
            resultCode = HttpClientUtil.sendPostRequest(sb.toString(), mess.toString(), "GBK");
            logger.info(resultCode);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("出现异常");
        } finally {
            SendMsg returnInf = new SendMsg();
            JSONObject jsonObject = JSONObject.fromObject(resultCode);
            returnInf.setResponseMessage(getResponse(jsonObject.getString("result")));
        }
        return resultCode;
    }

    public static boolean send(String phone, String content) {
        String resultCode = sendMess(phone, content);
        JSONObject jsonObject = JSONObject.fromObject(resultCode);
        return jsonObject.getString("result").equals("0");
    }

    /**
     * 将返回状态编码转化为描述结果
     *
     * @param result: 状态编码
     * @return 描述结果
     */
    private static String getResponse(String result) {
        if (result.equals("0")) {
            logger.info("发送成功");
            return "发送成功";
        }
        if (result.equals("100")) {
            logger.info("非法登录");
            return "非法登录";
        }
        if (result.equals("101")) {
            logger.info("连接过多");
            return "连接过多";
        }
        if (result.equals("102")) {
            logger.info("登陆类型错了");
            return "登陆类型错了";
        }
        if (result.equals("103")) {
            logger.info("协议版本号错了");
            return "协议版本号错了";
        }
        if (result.equals("104")) {
            logger.info("ip不合法");
            return "ip不合法";
        }
        if (result.equals("201")) {
            logger.info("非法手机号");
            return "非法手机号";
        }
        if (result.equals("204")) {
            logger.info("短信内容中有非法字符");
            return "短信内容中有非法字符";
        }
        if (result.equals("205")) {
            logger.info("短信内容太长");
            return "短信内容太长";
        }
        if (result.equals("206")) {
            logger.info("不存在的优先级");
            return "不存在的优先级";
        }
        if (result.equals("211")) {
            logger.info("http提交短信时的url不正确");
            return "http提交短信时的url不正确";
        }
        if (result.equals("500")) {
            logger.info("系统内部失败");
            return "系统内部失败";
        }
        if (result.equals("900")) {
            logger.info("余额不足");
            return "余额不足";
        }
        if (result.equals("901")) {
            logger.info("发送速度太快");
            return "发送速度太快";
        }
        return result;
    }

    /**
     * 主函数，测试一下功能
     * @param args
     */
    /*public static void main(String[] args) {
		SendMsg sendm=new SendMsg();
		sendm.sendMess("15827079875,15927073693,15827603218","尊敬的用户，您好!"); 
	}*/

}
