package me.materialdesign.utils;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import me.materialdesign.views.Point;

public class StringUtil {
	/**
	 * 
	 * @param str
	 * @return String
	 */
	public static String doEmpty(String str) {
		return doEmpty(str, "");
	}

	/**
	 * 
	 * @param str
	 * @param defaultValue
	 * @return String
	 */
	public static String doEmpty(String str, String defaultValue) {
		if (str == null || str.equalsIgnoreCase("null")
				|| str.trim().equals("") || str.trim().equals("null")) {
			str = defaultValue;
		} else if (str.startsWith("null")) {
			str = str.substring(4, str.length());
		}
		return str.trim();
	}

	private static final boolean DATA_TRANSFER_DEBUG = true;

	public static boolean notEmpty(Object o) {
		return o != null && !"".equals(o.toString().trim())
				&& !"null".equalsIgnoreCase(o.toString().trim())
				&& !"undefined".equalsIgnoreCase(o.toString().trim());
	}

	public static boolean empty(Object o) {
		return o == null || "".equals(o.toString().trim())
				|| "null".equalsIgnoreCase(o.toString().trim())
				|| "undefined".equalsIgnoreCase(o.toString().trim());
	}

	public static boolean num(Object o) {
		int n = 0;
		try {
			n = Integer.parseInt(o.toString().trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean decimal(Object o) {
		double n = 0;
		try {
			n = Double.parseDouble(o.toString().trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (n > 0.0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isNumeric(String str){
		for (int i = str.length(); --i >= 0;) {   
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param Jid
	 * @return
	 */
	public static String getUserNameByJid(String Jid) {
		if (empty(Jid)) {
			return null;
		}
		if (!Jid.contains("@")) {
			return Jid;
		}
		return Jid.split("@")[0];
	}

	/**
	 * 
	 * @param jidFor
	 * @param userName
	 * @return
	 */
	public static String getJidByName(String userName, String jidFor) {
		if (empty(jidFor) || empty(jidFor)) {
			return null;
		}
		return userName + "@" + jidFor;
	}


	/**
	 * 
	 * @param allDate
	 *            like "yyyy-MM-dd hh:mm:ss SSS"
	 * @return
	 */
	public static String getMonthTomTime(String allDate) {
		return allDate.substring(5, 19);
	}

	/**
	 * 
	 * @param allDate
	 *            like "yyyy-MM-dd hh:mm:ss SSS"
	 * @return
	 */
	public static String getMonthTime(String allDate) {
		return allDate.substring(5, 16);
	}
	

	
	// 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	// 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF,
	public static byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[src.length() / 2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}
	
	// 字节数组转十六进制字符串
	public static String getHexString(byte[] b){
		if(b == null) {
			return null;
		}
		
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	

	
	public static String printableBytes(byte[] bytes) {
		if (DATA_TRANSFER_DEBUG) {
			StringBuffer sb = new StringBuffer();
			int count = 0;
			int maxLen = 16384;
			if (bytes.length < maxLen) {
				maxLen = bytes.length;
			}
			for(int i = 0; i < maxLen; i++){
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1) + " ");
				count += 1;
				if (count == 16) {
					sb.append("\r\n");
					count = 0;
				}
			}
			return sb.toString();
		}
		else {
			return "";
		}
	}
	
	public static byte[] revertByteArray(byte[] in) {
		int len = in.length;
		byte[] out = new byte[len];
		
		for(int i = 0; i < len; i++) {
			out[i] = in[len - i - 1];
		}
		
		return out;
	}
	

	public static String formatFileNmae(String filename){
		if(empty(filename)){
			return null;
		}
		if(filename.length()<=10){
			return filename;
		}else{
			String t = filename.substring(0, 5);
			String w = filename.substring(filename.length()-5,filename.length());
			return t+"..."+w;
		}
	}
	
	/**
	 * 
	 * @param list
	 * @param str
	 * @return
	 * @add-date 2015-7-6
	 * @version dev_br_1.1.0.150706
	 * @author OF
	 */
	public static boolean isStringInList(List<String> list,String str){
		boolean in =  false;
		if(list == null || str == null){
			return in;
		}else{
			for(String s:list){
				if(str.equals(s)){
					return true;
				}
			}
		}
		return in;
	}
	
	/**
	 * 
	 * @param array
	 * @return
	 * @add-date 2015-7-6
	 * @version dev_br_1.1.0.150705
	 * @author OF
	 */
	public static ArrayList<String> jArrayToArrayList(JSONArray array){
		ArrayList<String> list = new ArrayList<String>();
		if(array == null){
			return list;
		}
		for(int i = 0;i <array.length();i++){
			try {
				list.add(array.getString(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 用于判断两次绘制的手势密码是否一致
	 * @param l1
	 * @param l2
	 * @return
	 */
	public static boolean isSamePattern(ArrayList<Point> l1,ArrayList<Point> l2){
		if(l1 == null || l2 == null){
			return  false;
		}
		if(l1.size() != l2.size()){
			return  false;
		}
		for(int i =0;i < l2.size();i++){
			Point p1 = l1.get(i);
			Point p2 = l2.get(i);
			if(p1.getValue().equals(p2.getValue())){
				continue;
			}else{
				return  false;
			}

		}
		return true;
	}

	/**
	 * 根据手势获取当前的密码
	 * @param list
	 * @return
	 */
	public static String getGestureFromPoints(ArrayList<Point> list){
		if(list == null){
			return null;
		}else{
			StringBuilder sb = new StringBuilder();
			for(Point p : list){
				sb.append(p.getValue());
			}
//			return sb.toString();
			return MD5Util.MD5Encode(sb.toString());
		}

	}
}
