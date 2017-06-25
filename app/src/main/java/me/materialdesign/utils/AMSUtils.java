package me.materialdesign.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import me.materialdesign.activities.ProxyActivity;
import me.materialdesign.activities.ProxyOtherActivity;

/**
 * Created by Administrator on 2017/6/24 0024.
 */

public class AMSUtils {

    private Class<?> proxyActivity;
    private Context context;
    private Object activityThreadValue;
    private String TAG = "AMSUtils";

    public AMSUtils(Class<?> proxyActivity,Context context){
        this.proxyActivity  = proxyActivity;
        this.context = context;
    }

    public void  hookAMS(){
        Class<?> aClass = null;
        try {
            aClass = Class.forName("android.app.ActivityManagerNative");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Field gDefault = null;
        try {
            gDefault = aClass.getDeclaredField("gDefault");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        gDefault.setAccessible(true);
        Object defaultValue = null;
        try {
            //静态的，没有实例
            defaultValue = gDefault.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Class<?> aClass1 = null;
        try {
            aClass1 = Class.forName("android.util.Singleton");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Field mInstance = null;
        try {
            mInstance = aClass1.getDeclaredField("mInstance");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        mInstance.setAccessible(true);
        Object iActivityManager = null;
        try {
            iActivityManager = mInstance.get(defaultValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Class<?> proxyActivityManager = null;
        try {
            proxyActivityManager = Class.forName("android.app.IActivityManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        AMSInvocationHandler handler = new AMSInvocationHandler(iActivityManager);
        Object proxy = Proxy.newProxyInstance(context.getClassLoader(),new Class<?>[]{proxyActivityManager},handler);
        try {
            mInstance.set(defaultValue,proxy);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String before = ParseJarFile.getSignature(context,context.getPackageName());
        Log.i(TAG,"before hook:"+before);
        modifyPackageSignature();
    }


    class AMSInvocationHandler implements InvocationHandler{

        private Object iActivityManagerValue;

        public AMSInvocationHandler(Object iActivityManagerValue) {
            this.iActivityManagerValue = iActivityManagerValue;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] args) throws Throwable {
            if ("startActivity".equals(method.getName())) {
                Intent intent = null;
                int index = 0;
                for (int i =0;i<args.length;i++) {
                    if (args[i] instanceof Intent) {
                        intent = (Intent) args[i];
                        index = i;
                        break;
                    }
                }
                Intent proxyIntent = new Intent();
                ComponentName cmp = new ComponentName(context, ProxyActivity.class);
                proxyIntent.putExtra("oldIntent",intent);
                proxyIntent.setComponent(cmp);
                args[index] = proxyIntent;
                return method.invoke(iActivityManagerValue,args);
            }
            return method.invoke(iActivityManagerValue,args);
        }
    }

    public void hookSystemHandler(){
        try {
            Class<?> aClasss = Class.forName("android.app.ActivityThread");
            try {
                Method activtyThread = aClasss.getDeclaredMethod("currentActivityThread");
                activtyThread.setAccessible(true);
                Object activityThreadValue = activtyThread.invoke(null);
                Field mH = aClasss.getDeclaredField("mH");
                mH.setAccessible(true);
                Handler handlerObject = (Handler) mH.get(activityThreadValue);
                Field mCallback = Handler.class.getDeclaredField("mCallback");
                mCallback.setAccessible(true);
                mCallback.set(handlerObject, new ActivityThreadHandlerCallback(handlerObject));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    class ActivityThreadHandlerCallback implements android.os.Handler.Callback{
        private Handler handler;

        public ActivityThreadHandlerCallback(Handler handler) {
            this.handler = handler;
        }

        @Override
        public boolean handleMessage(Message message) {

            if (message.what == 100) {
                handleLaunchActivity(message);
            }
            handler.handleMessage(message);
            return true;
        }
        public void handleLaunchActivity(Message msg){
            Object obj = msg.obj;
            try {
                Field intentFiled = obj.getClass().getDeclaredField("intent");
                intentFiled.setAccessible(true);
                Intent proxyIntent = (Intent) intentFiled.get(obj);
                Intent realIntent = proxyIntent.getParcelableExtra("oldIntent");
                if (realIntent != null) {
                    intentFiled.set(obj,realIntent);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public void modifyPackageSignature(){
        try {
            Class<?> pkgMgrCls = Class.forName("android.app.ActivityThread");
            Method activtyThread = pkgMgrCls.getDeclaredMethod("getPackageManager");
            activtyThread.setAccessible(true);
            Object mgr = activtyThread.invoke(null);
            Field sPackageMgr = pkgMgrCls.getDeclaredField("sPackageManager");
            sPackageMgr.setAccessible(true);
            Log.i(TAG,"check mgr hashcode:"+mgr.hashCode());
            Object ipkgMgr = sPackageMgr.get(mgr);
            Class<?> ipkgMgrCls = Class.forName("android.content.pm.IPackageManager");
            PackageManagerHandler handler  = new PackageManagerHandler(ipkgMgr);
            Object pkgProxy = Proxy.newProxyInstance(context.getClassLoader(),new Class[]{ipkgMgrCls},handler);
            sPackageMgr.set(mgr,pkgProxy);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    class PackageManagerHandler implements InvocationHandler {

        private Object proxyClass;

        public PackageManagerHandler(Object proxyClass) {
            this.proxyClass = proxyClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("getPackageInfo".equals(method.getName())) {
                PackageInfo packageInfo = (PackageInfo) method.invoke(proxyClass,args);
                Signature[] sis = new Signature[1];
                sis[0] = new Signature("this is test signature".getBytes());
                new Exception("check getPackageInfo-invoke").printStackTrace();
                packageInfo.signatures = sis;
                return packageInfo;
            }
            return method.invoke(proxyClass,args);
        }
    }

}
