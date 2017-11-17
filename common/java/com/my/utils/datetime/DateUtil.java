package com.my.utils.datetime;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 日期转换为字符串，格式为(yyyy-MM-dd)
	 * 
	 * @param date
	 *            日期
	 */
	public static String date2String(Date date) {
		return date!=null?new SimpleDateFormat("yyyy-MM-dd").format(date):"";
	}
	
	/**
	 * 日期转换为字符串，格式为(yyyyMMdd)
	 * 
	 * @param date
	 *            日期
	 */
	public static String date2StringNoSeparator(Date date){
		return date!=null?new SimpleDateFormat("yyyyMMdd").format(date):"";
	}
	
	/**
	 * 日期转换为日期时间字符串，格式为(yyyy-MM-dd hh:mm:ss)
	 * 
	 * @param date
	 *            日期
	 */
	public static String dateTime2String(Date date) {
		return date!=null?new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date):"";
	}

	/**
	 * 日期转换为中文日期字符串，格式为(yyyy年MM月dd号)
	 * 
	 * @param date
	 *            日期
	 */
	public static String date2StringForCN(Date date) {
		return new SimpleDateFormat("yyyy年MM月dd号").format(date);
	}

	/**
	 * 设置相应的年月日时间，返回中文日期字符串，格式为(yyyy-MM-dd)，年月日只设置其中一个，如设置多个，会导致最终结果不正确
	 * 
	 * @param date
	 *            日期
	 * @param year
	 *            需要调整的年份,如无需调整则设置值为0
	 * @param month
	 *            需要调整的月份,如无需调整则设置值为0
	 * @param day
	 *            需要调整的天数,如无需调整则设置值为0
	 * @return
	 */
	public static String date2String(Date date, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, year);
		c.add(Calendar.MONTH, month);
		c.add(Calendar.DAY_OF_MONTH, day);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}

	/**
	 * 设置相应的年月日时间，返回中文日期字符串，格式为(yyyy年MM月dd号)，年月日只设置其中一个，如设置多个，会导致最终结果不正确
	 * 
	 * @param date
	 *            日期
	 * @param year
	 *            需要调整的年份,如无需调整则设置值为0
	 * @param month
	 *            需要调整的月份,如无需调整则设置值为0
	 * @param day
	 *            需要调整的天数,如无需调整则设置值为0
	 * @return
	 */
	public static String date2StringForCN(Date date, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, year);
		c.add(Calendar.MONTH, month);
		c.add(Calendar.DAY_OF_MONTH, day);
		return new SimpleDateFormat("yyyy年MM月dd号").format(c.getTime());
	}

	/**
	 * 设置相应的年月日时间，返回中文日期字符串，格式为(MM月dd号)，年月日只设置其中一个，如设置多个，会导致最终结果不正确
	 * 
	 * @param date
	 *            日期
	 * @param year
	 *            需要调整的年份,如无需调整则设置值为0
	 * @param month
	 *            需要调整的月份,如无需调整则设置值为0
	 * @param day
	 *            需要调整的天数,如无需调整则设置值为0
	 * @return
	 */
	public static String date2MonthStringForCN(Date date, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, year);
		c.add(Calendar.MONTH, month);
		c.add(Calendar.DAY_OF_MONTH, day);
		return new SimpleDateFormat("MM月dd号").format(c.getTime());
	}

	/**
	 * 返回当前日期，格式为yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/**
	 * 返回当前日期时间，格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	/**
	 * 比较两个日期值，返回两个日期相差的天数
	 * @param dateOne	日期一
	 * @param dateTwo	日期二
	 * @return
	 */
	public static int comparisonDate(Date dateOne,Date dateTwo) {
		long millisecond = dateOne.getTime()-dateTwo.getTime();
		return (int)millisecond/1000/60/60/24;
	}
	
	/**
	 * 返回当前日期时间，格式为yyyy年MM月dd日 HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTimeForCN() {
		return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date());
	}
	
	/**
	 * 返回当前日期时间，格式为yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String getTime(){
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	/***
	 * 在某日期基础上增加天数
	 * @param datetime：日期
	 * @param parrten：日期格式
	 * @param days：增加天数
	 * @return
	 */
	public static String addTime(String datetime, String parrten, long days) {
		SimpleDateFormat formatter = new SimpleDateFormat(parrten);
		ParsePosition pos = new ParsePosition(0);
		Date dt1 = formatter.parse(datetime, pos);
		long l = dt1.getTime() / 1000L + days * 24L * 60L * 60L;
		dt1.setTime(l * 1000L);
		String mydate = formatter.format(dt1);
		return mydate;
	}
	
	/***
	 * 根据指定格式获取当前日期
	 * @param parrten:格式
	 * @return
	 */
	public static String getTime(String parrten) {
		if (parrten == null || parrten.equals(""))
			parrten = "yyyyMMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(parrten);
		Date cday = new Date();
		String timestr = sdf.format(cday);
		return timestr;
	}
	
	/***
	 * 字符串转日期型
	 * @param time:日期字串
	 * @param parrten:格式
	 * @return
	 */
	public static Date parseDateTime(String time, String parrten) {
		if (parrten == null || parrten.equals(""))
			parrten = "yyyy-MM-dd";
		SimpleDateFormat formatter = new SimpleDateFormat(parrten);
		ParsePosition pos = new ParsePosition(0);
		Date dt1 = formatter.parse(time, pos);
		return dt1;
	}
}
