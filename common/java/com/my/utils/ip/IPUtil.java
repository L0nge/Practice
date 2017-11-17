package com.my.utils.ip;

import com.common.utils.UnicodeUtil;
import com.common.utils.json.JsonUtil;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 根据IP地址获取详细的地域信息，也可通过 http://whois.pconline.com.cn/ 获取地址信息
 */
public class IPUtil {
	/**
	 * 获取IP所在地址
	 */
	public static String getAddresses(String content, String encodingString) throws UnsupportedEncodingException {
		// 淘宝的接口
		String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
		String returnStr = IPUtil.getResult(urlStr, content, encodingString);
		if (returnStr != null) {
			// 处理返回的省市区信息
			System.out.println(returnStr);
			JSONObject obj = JsonUtil.toJSONObject(returnStr);
			JSONObject dataObj = JsonUtil.toJSONObject(obj.get("data"));
			String code = obj.get("code").toString();
			if ("1".equals(code)) {
				return "0";	// 无效IP，局域网测试
			}
			String region = UnicodeUtil.decode(dataObj.get("region").toString());	//省份
			String country = UnicodeUtil.decode(dataObj.get("country").toString());	//国家
			String area = UnicodeUtil.decode(dataObj.get("area").toString());		//地区
			String city = UnicodeUtil.decode(dataObj.get("city").toString());		//城市
			String isp = UnicodeUtil.decode(dataObj.get("isp").toString());			//公司

			System.out.println(country + "=" + area + "=" + region + "=" + city + "=" + isp);
			return region;
		}
		return null;
	}

	/**
	 * 请求接口
	 */
	private static String getResult(String urlStr, String content, String encoding) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();	// 新建连接实例
			connection.setConnectTimeout(10000);					// 设置连接超时时间，单位毫秒
			connection.setReadTimeout(10000);						// 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true);							// 是否打开输出流 true|false
			connection.setDoInput(true);							// 是否打开输入流true|false
			connection.setRequestMethod("POST");					// 提交方法POST|GET
			connection.setUseCaches(false);							// 是否缓存true|false
			connection.connect();									// 打开连接端口
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
			out.writeBytes(content);								// 写数据,也就是提交你的表单
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
			StringBuffer buffer = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();// 关闭连接
			}
		}
		return null;
	}

	// 测试
	public static void main(String[] args) {
		String ip = "110.75.244.156";
		try {
			IPUtil.getAddresses("ip=" + ip, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}