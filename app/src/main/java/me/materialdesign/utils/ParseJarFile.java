package me.materialdesign.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by OF on 2017/6/25 0025.
 */

public class ParseJarFile {

    private static String TAG = "ParseJarFile";
    public static void parseApk(String archiveFilePath, Context context){
        Signature[] signaturesArray = getSignaturesByApk(archiveFilePath,context);

        StringBuilder builder = new StringBuilder();
        for (Signature signature : signaturesArray) {
            builder.append(signature.toCharsString());
        }
        /************** 得到应用签名 **************/
        String signature = builder.toString();
        Log.i(TAG,"we get signatures by myself:"+signature);
        String getFromApi = getSignature(context,context.getPackageName());
        Log.i(TAG,"we get signatures from api:"+getFromApi+",is equal:"+signature.equals(getFromApi));

    }

    public static Signature[] getSignaturesByApk(String apkPath,Context context){
        try {
            Log.i(TAG,"we need check apk file path:"+apkPath);
            Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
            Constructor<?> packageConstructor = packageParserClass.getConstructor(String.class);
            Object packageParserInstance = packageConstructor.newInstance(apkPath);
//            for (Method m : packageParserClass.getDeclaredMethods()) {
//                Log.i(TAG,"we need check declared method name:"+m.getName());
//            }
            Method parseMethod = packageParserClass.getDeclaredMethod("parsePackage",File.class,String.class,DisplayMetrics.class,int.class);
            parseMethod.setAccessible(true);
            final File apkFile = new File(apkPath);
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            Object packageObject = parseMethod.invoke(packageParserInstance,apkFile,apkFile.getParentFile().getAbsoluteFile()+"/decode",metrics,0);
            Class<?> packageClass = Class.forName("android.content.pm.PackageParser$Package");
            Method collectCertificateMethod = packageParserClass.getDeclaredMethod("collectCertificates",packageClass,int.class);
            collectCertificateMethod.setAccessible(true);
            collectCertificateMethod.invoke(packageParserInstance,packageObject,0);
            Field signatures = packageClass.getDeclaredField("mSignatures");
            signatures.setAccessible(true);
            Signature[] signaturesArray = (Signature[])signatures.get(packageObject);
            return signaturesArray;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getSignature(Context context,String pkgname) {
        {
            try {
                /** 通过包管理器获得指定包名包含签名的包信息 **/
                PackageManager mgr = context.getPackageManager();
                PackageInfo packageInfo = mgr.getPackageInfo(pkgname,PackageManager.GET_SIGNATURES);
                Log.i(TAG,"check mgr hashcode:"+mgr.hashCode());

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
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
