package me.materialdesign.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 *
 * @author shimiso
 */

@SuppressLint("SimpleDateFormat")
public class DateUtil {

	public static final String FORMAT = "yyyy-MM-dd HH:mm:ss SSS";
	public static final String SIMPLE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String LOG_TIME = "yyyy-MM-dd-HH-mm-ss";
	public static Date str2Date(String str) {
		return str2Date(str, null);
	}

	public static Date str2Date(String str, String format) {
		if (str == null || str.length() == 0) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

	}

	public static Calendar str2Calendar(String str) {
		return str2Calendar(str, null);

	}

	public static Calendar str2Calendar(String str, String format) {

		Date date = str2Date(str, format);
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c;

	}
	public static long str2Long(String str, String format) {

		Date date = str2Date(str, format);
		if (date == null) {
			return 0;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c.getTimeInMillis();

	}

	public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
		return date2Str(c, null);
	}

	public static String date2Str(Calendar c, String format) {
		if (c == null) {
			return null;
		}
		return date2Str(c.getTime(), format);
	}

	public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
		return date2Str(d, null);
	}

	public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
		if (d == null) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s = sdf.format(d);
		return s;
	}

	public static String getCurDateStr() {
		Calendar c = Calendar.getInstance();
		return date2Str(c);
	}
	
	public static long getCurDateLong() {
		Calendar c = Calendar.getInstance();
		return c.getTimeInMillis();
	}
	//modify log:bug-fix:#1818.--OF
	/** 
	 * 
	 * Get BeiJing Time.
	 * @return  0:get BeiJing time error. other:ok
	 */
	public static long getInternetTimeLong() {
		long ld = 0;
		URL url = null;
		try {
			url = new URL("http://www.taobao.com/");
		} catch (MalformedURLException e) {
			Log.d("DateUtil", "we get network time:encounter MalformedURL exception");
			e.printStackTrace();
			return 0;
		}
		HttpURLConnection uc = null;
		try {
			uc = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			Log.d("DateUtil", "we get network time:encounter IO exception");
			e.printStackTrace();
			return 0;
		}
		try {
			uc.setRequestMethod("GET");
			uc.setConnectTimeout(2000);
			uc.connect();
			ld =uc.getDate();
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		Log.d("DateUtil", "we get network time:"+DateUtil.getMillon(ld));
		return ld;
	}

	/**
	 *
	 * @param format
	 * @return
	 */
	public static String getCurDateStr(String format) {
		Calendar c = Calendar.getInstance();
		return date2Str(c, format);
	}

	public static String getMillon(long time) {

		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);

	}

	public static String getDay(long time) {

		return new SimpleDateFormat("yyyy-MM-dd").format(time);

	}

	public static String getSMillon(long time) {

		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);

	}
	/**
	 * 
	 * @param time
	 * @return
	 * @add-date 2015-5-7
	 * @version dev_br_1.4.3.150508
	 * @author OF
	 */
	public static String getSMillon2(long time) {
		
		return new SimpleDateFormat(FORMAT).format(time);
		
	}
	
	public static String getAudioDur(int time){
		if(time>60&&time<3600){
			int m = time/60;
			int s = time - 60*m;
			return m+"'"+s+"''";
		}else if(time>=3600){
			int h = time/3600;
			int timeSurplus = time - h*3600;
			int m = timeSurplus/60;
			int s = timeSurplus - 60*m;
			return h+":"+m+"'"+s+"''";		
		}else{
			return time+"''";
		}
	}
	
	/**
	 * Get format for yyyy-MM-dd HH:mm:ss SSS  the date
	 * @param time:long
	 * @return  yyyy-MM-dd HH:mm:ss SSS  date 
	 */
	public static String getDate(long time){
		return new SimpleDateFormat(FORMAT).format(time);
	}
	public static String getDate(long time,String format){
		return new SimpleDateFormat(format).format(time);
	}
	/**
	 * Get  InternetTime to format for yyyy-MM-dd HH:mm:ss SSS the Date
	 * @return
	 */
	public static String getInternetDateStr(){
		return getDate(getInternetTimeLong());
	}
	
}
