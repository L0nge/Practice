package com.my.utils;

import com.common.utils.config.GlobalConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>基本正则验证</h2>
 */
public class Regex {
	/**
	 * <h2>判断字符串是否为手机号码</h2>
	 * 
	 * @param str
	 *            手机号码
	 * @return
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * <h2>判断字符串是否为数字格式</h2>
	 * <p>
	 * 只判断带两位小数点的数字
	 * </p>
	 * 
	 * @param str
	 *            金额
	 * @return
	 */
	public static boolean isNumber(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 验证金额
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * <h2>判断日期格式是否正确</h2>
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isDate(String date) {
		if (StringUtil.isNullAndEmpty(date)) {
			return false;
		} else {
			Pattern p = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-]?((((0?[13578])|(1[02]))[\\-]?"
					+ "((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]?"
					+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]?"
					+ "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-]?"
					+ "((((0?[13578])|(1[02]))[\\-]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]?"
					+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
			Matcher matcher = p.matcher(date);
			return matcher.matches();
		}

	}

	/**
	 * <h2>判断日期时间格式是否正确</h2>
	 */
	public static boolean isDateTime(String dateTime) {
		if (StringUtil.isNullAndEmpty(dateTime)) {
			return false;
		} else {
			Pattern p = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-]?((((0?[13578])|(1[02]))[\\-]?"
					+ "((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]?"
					+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]?"
					+ "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-]?"
					+ "((((0?[13578])|(1[02]))[\\-]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]?"
					+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]?" + "((0?[1-9])|(1[0-9])|(2[0-8]))))))"
					+ "(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
			Matcher matcher = p.matcher(dateTime);
			return matcher.matches();
		}
	}

	/**
	 * 根据Unicode编码完美的判断中文汉字和符号
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/**
	 * 完整的判断中文汉字和符号
	 * 
	 * @param strName
	 * @return
	 */
	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 只能判断部分CJK字符（CJK统一汉字）
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChineseByREG(String str) {
		if (str == null) {
			return false;
		}
		Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
		return pattern.matcher(str.trim()).find();
	}

	/**
	 * 只能判断部分CJK字符（CJK统一汉字）
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChineseByName(String str) {
		if (str == null) {
			return false;
		}
		// 大小写不同：\\p 表示包含，\\P 表示不包含
		// \\p{Cn} 的意思为 Unicode 中未被定义字符的编码，\\P{Cn} 就表示 Unicode中已经被定义字符的编码
		String reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}";
		Pattern pattern = Pattern.compile(reg);
		return pattern.matcher(str.trim()).find();
	}
	
	/**
	 * 判断手机归属地，若手机号码不存在，则需重新输入
	 * @param mobile 需输入完整的手机号码
	 * @return
	 */
	public static String belongResult(String mobile){
		String result=null;
		if(isMobile(mobile)){
		String url=String.format(GlobalConfig.URL, mobile);
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements els = doc.getElementsByClass("tdc2");
		String belong=els.get(1).text();
			if(belong.equals("未知")){
			    result="该号码不存在";
			}else {
				result="归属地：" + els.get(1).text();
			}
		
		}
		else{
			result="验证手机号有误,请输入完整且准确的手机号码";
		}
		return result;
	}
	
	/**
	 * 判断手机号码类型，若手机号码不存在，则需重新输入
	 * @param mobile 需输入完整的手机号码
	 * @return
	 */
	public static String serviceResult(String mobile){
		String result=null;
		if(isMobile(mobile)){
			String url=String.format(GlobalConfig.URL, mobile);
			Document doc = null;
			try {
				doc = Jsoup.connect(url).get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Elements els = doc.getElementsByClass("tdc2");
			String service=els.get(2).text();
			if(service.contains("移动")){
				result="运营商：" + "移动";
			}else if (service.contains("电信")){
				result="运营商：" + "电信";
			}else if(service.contains("联通")){
				result="运营商：" + "联通";
			}else {
				result="该号码不存在";
			}
			
		}else
		{
			result="验证手机号有误,请输入完整且准确的手机号码";
		}
		return result;
	}
	
	public static boolean phoneIdentify(String phonenumber){
		
		//phoneService.findByTel(phonenumber);
		return true;
	}
	

	public static void main(String[] args) {		
	}
}