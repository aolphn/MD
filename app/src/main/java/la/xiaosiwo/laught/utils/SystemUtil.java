/*
 * Copyright (c) 2014, KJFrameForAndroid 张涛 (kymjs123@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package la.xiaosiwo.laught.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.security.MessageDigest;


/**
 * 系统信息工具包<br>
 * 
 * <b>创建时间</b> 2014-8-14
 * 
 * @author kymjs(kymjs123@gmail.com)
 * @version 1.1
 */
public final class SystemUtil {
	/**
	 * 屏幕的宽度
	 */
	public static int screenWidth;
	/**
	 * 屏幕的高度
	 */
	public static int screenHeight;
	public static int getSignatureHash(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(),
							PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];			
			int hashcode = sign.hashCode();
			Log.i("test", "hashCode : " + hashcode);
			return hashcode ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	 public static String getSignature(Context context,String pkgname) {
	     {
	           try {
	               /** 通过包管理器获得指定包名包含签名的包信息 **/
	        	   PackageInfo packageInfo =context.getPackageManager()
	                       .getPackageInfo(pkgname,
	                               PackageManager.GET_SIGNATURES);
	               /******* 通过返回的包信息获得签名数组 *******/
	        	   Signature[] signatures = packageInfo.signatures;
	               /******* 循环遍历签名数组拼接应用签名 *******/
	        	   StringBuilder builder = new StringBuilder();
	               for (Signature signature : signatures) {
	                   builder.append(signature.toCharsString());
	               }
	               /************** 得到应用签名 **************/
	               String signature = builder.toString();
	               return signature;
	           } catch (NameNotFoundException e) {
	               e.printStackTrace();
	               return null;
	           }
	       }
	   }

    /**
     * 获取应用签名
     * 
     * @param context
     * @param pkgName
     */
    public static String getSign32bit(Context context, String pkgName) {
        try {
            PackageInfo pis = context.getPackageManager()
                    .getPackageInfo(pkgName,
                            PackageManager.GET_SIGNATURES);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 将签名字符串转换成需要的32位签名
     */
    private static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = { 48, 49, 50, 51, 52, 53, 54, 55,
                                  56, 57, 97, 98, 99, 100, 101, 102 };
        try {
            MessageDigest localMessageDigest = MessageDigest
                    .getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0;; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
        }
        return "";
    }
    
    public static int screenWidth(Context context){
    	WindowManager manager = (WindowManager) context 
                .getSystemService(Context.WINDOW_SERVICE); 
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
    public static int screenHeight(Context context){
    	WindowManager manager = (WindowManager) context 
                .getSystemService(Context.WINDOW_SERVICE); 
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }
}