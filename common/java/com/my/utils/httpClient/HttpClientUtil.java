package com.my.utils.httpClient;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 封装了采用HttpClient发送HTTP请求的方法
 */

public class HttpClientUtil {
	/**
	 * http请求发送失败
	 */
	public static final int SENDFAIL = 0;

	/**
	 * http请求发送成功
	 */
	public static final int SENDSUC = 1;

	/**
	 * http请求超时
	 */
	public static final int TIMEOUT = 3;

	private HttpClientUtil() {}

	/**
	 * 发送HTTP_GET请求
	 * @see 1)该方法会自动关闭连接,释放资源
	 * @see 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
	 * @see 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决定传入前是否转码
	 * @see 4)该方法会自动获取到响应消息头中[Content-Type:text/html;charset=GBK]的charset值作为响应报文的解码字符集
	 * @see 5)若响应消息头中无Content-Type属性,则自己指定字符集作为响应报文的解码字符集(如传空值，则默认用ISO8859-1)
	 * @param reqURL 请求地址(含参数)
	 * @param encodeName 返回字符集格式
	 * @return 远程主机响应正文
	 */
	public static String sendGetRequest(String reqURL, String encodeName) {
		String respContent = String.valueOf(SENDFAIL); // 响应内容
		HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
		// 设置代理服务器
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000); // 连接超时5s
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000); // 读取超时5s
		HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet
		try {
			HttpResponse response = httpClient.execute(httpGet); // 执行GET请求
			HttpEntity entity = response.getEntity(); // 获取响应实体
			if (null != entity) {
				Charset respCharset = ContentType.getOrDefault(entity).getCharset();
				if (respCharset == null) {
					if (StringUtils.isNotEmpty(encodeName)) {
						respCharset = Charset.forName("UTF-8");
					}
				}
				
				boolean isGzip = false;
				for(org.apache.http.Header header : response.getAllHeaders()){
					if("Content-Encoding".equals(header.getName().trim()) && "gzip".equals(header.getValue().trim().toLowerCase())){//为gzip包数据
						isGzip = true;
					}
				}
				
				if(isGzip){
					GzipDecompressingEntity gzipDecompressingEntity = new GzipDecompressingEntity(entity);
					respContent = EntityUtils.toString(gzipDecompressingEntity, respCharset);
				}
				else{
					respContent = EntityUtils.toString(entity, respCharset);
				}
				
				
				// Consume response content
				EntityUtils.consume(entity);
			}
			// 获取应答报文头
			// StringBuilder respHeaderDatas = new StringBuilder();
			// for(Header header : response.getAllHeaders()){
			// respHeaderDatas.append(header.toString()).append("\r\n");
			// }
			// String respStatusLine = response.getStatusLine().toString(); //
			// HTTP应答状态行信息
		} catch (ConnectTimeoutException cte) { // 连接超时
			cte.printStackTrace();
		} catch (SocketTimeoutException ste) { // 响应超时
			respContent = String.valueOf(TIMEOUT);
			ste.printStackTrace();
		} catch (ClientProtocolException cpe) { // 协议错误
			cpe.printStackTrace();
		} catch (ParseException pe) { // 请求通行时解析错误
			pe.printStackTrace();
		} catch (IOException ioe) { // 网络错误
			ioe.printStackTrace();
		} catch (Exception e) { // 其他错误
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpClient.getConnectionManager().shutdown();
		}
		return respContent;
	}

	/**
	 * 发送HTTP_POST请求
	 * @see 1)该方法允许自定义任何格式和内容的HTTP请求报文体
	 * @see 2)该方法会自动关闭连接,释放资源
	 * @see 3)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
	 * @see 4)请求参数含中文等特殊字符时,可直接传入本方法,并指明其编码字符集encodeCharset参数,方法内部会自动对其转码
	 * @see 5)该方法在解码响应报文时所采用的编码,取自响应消息头中的[Content-Type:text/html; charset=GBK]的charset值
	 * @see 6)若响应消息头中未指定Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1
	 * @param reqURL 请求地址
	 * @param reqData 请求参数,若有多个参数则应拼接为param11=value11&22=value22&33=value33的形式
	 * @param encodeCharset 编码字符集,编码请求数据时用之,此参数为必填项(不能为""或null)
	 * @return 远程主机响应正文
	 */
	public static String sendPostRequest(String reqURL, String reqData, String encodeCharset) {
		String reseContent = "通信失败";
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
		HttpPost httpPost = new HttpPost(reqURL);
		// 由于下面使用的是new
		// StringEntity(....),所以默认发出去的请求报文头中CONTENT_TYPE值为text/plain;
		// charset=ISO-8859-1
		// 这就有可能会导致服务端接收不到POST过去的参数,比如运行在Tomcat6.0.36中的Servlet,所以我们手工指定CONTENT_TYPE头消息
		httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=" + encodeCharset);
		try {
			httpPost.setEntity(new StringEntity(reqData == null ? "" : reqData, encodeCharset));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				reseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
				EntityUtils.consume(entity);
			}
		} catch (ConnectTimeoutException cte) {
			cte.printStackTrace();
		} catch (SocketTimeoutException ste) {
			ste.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return reseContent;
	}

	/**
	 * 发送HTTP_POST请求
	 *
	 * @see 1)该方法允许自定义任何格式和内容的HTTP请求报文体
	 * @see 2)该方法会自动关闭连接,释放资源
	 * @see 3)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
	 * @see 4)请求参数含中文等特殊字符时,可直接传入本方法,并指明其编码字符集encodeCharset参数,方法内部会自动对其转码
	 * @see 5)该方法在解码响应报文时所采用的编码,取自响应消息头中的[Content-Type:text/html;
	 *      charset=GBK]的charset值
	 * @see 6)若响应消息头中未指定Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1
	 * @param reqURL
	 *            请求地址
	 * @param reqData
	 *            请求参数,若有多个参数则应拼接为param11=value11&22=value22&33=value33的形式
	 * @param encodeCharset
	 *            编码字符集,编码请求数据时用之,此参数为必填项(不能为""或null)
	 * @return 远程主机响应正文
	 */
	public static String sendPostRequest(String reqURL, Map reqData, String encodeCharset) throws UnsupportedEncodingException {
		String reseContent = "通信失败";
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		HttpPost httpPost = new HttpPost(reqURL);
		// 由于下面使用的是new
		// StringEntity(....),所以默认发出去的请求报文头中CONTENT_TYPE值为text/plain;
		// charset=ISO-8859-1
		// 这就有可能会导致服务端接收不到POST过去的参数,比如运行在Tomcat6.0.36中的Servlet,所以我们手工指定CONTENT_TYPE头消息
		httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=" + encodeCharset);
		try {
			//设置参数
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			Iterator iterator = reqData.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
				list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
			}
			if (list.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
				httpPost.setEntity(entity);
			}
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				reseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
				EntityUtils.consume(entity);
			}
		} catch (ConnectTimeoutException cte) {
			cte.printStackTrace();
		} catch (SocketTimeoutException ste) {
			ste.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return reseContent;
	}
}
