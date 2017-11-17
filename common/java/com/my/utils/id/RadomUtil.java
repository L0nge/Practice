package com.my.utils.id;

import java.util.Random;
import java.util.UUID;

/***
 * 随机生成数
 */
public class RadomUtil {
	/***
	 * 随机生成4位随机数
	 * @return
	 */
	public static synchronized String generateRandom_4() throws Exception{
		StringBuffer sb = new StringBuffer();
        String str = "0123456789";
        Random r = new Random();
        for(int i=0;i<4;i++){
            int num = r.nextInt(str.length());
            sb.append(str.charAt(num));
            str = str.replace((str.charAt(num)+""), "");
        }
        return sb.toString();
	}
	
	/***
	 * 随机生成6位随机数
	 * @return
	 */
	public static synchronized String generateRandom_6() throws Exception{
		StringBuffer sb = new StringBuffer();
        String str = "0123456789";
        Random r = new Random();
        for(int i=0;i<6;i++){
            int num = r.nextInt(str.length());
            sb.append(str.charAt(num));
            str = str.replace((str.charAt(num)+""), "");
        }
        return sb.toString();
	}
	
	/***
	 * 随机生成14位随机数
	 * @return
	 */
	public static synchronized String generateRandom_14() throws Exception{
		String str = UUID.randomUUID().toString().replace("-", "");
		String s = str.substring(str.length()-14);
		return s;
	}
	
	/***
	 * 随机生成20位的随机订单号
	 * @return
	 */
	public static synchronized String getOrderId() throws Exception{
		String str = UUID.randomUUID().toString().replace("-", "");
		String s = str.substring(str.length()-20);
		return s;
	}

	public static synchronized String generateRandom_16() throws Exception{
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		for(int i=0;i<15;i++){
			int num = r.nextInt(10);
			sb.append(num);
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	public static void main(String[] args) {
		try {
			System.out.println(RadomUtil.getOrderId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
