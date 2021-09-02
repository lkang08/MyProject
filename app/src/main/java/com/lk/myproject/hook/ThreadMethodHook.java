package com.lk.myproject.hook;

import android.util.Log;

import dalvik.system.DexFile;
import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;

public class ThreadMethodHook extends XC_MethodHook {
    public static void testHook() {
        testHook1();
    }

    static void testHook3() {
        DexposedBridge.findAndHookMethod(DexFile.class, "loadDex", String.class, String.class, int.class,
                new ThreadMethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        String dex = (String) param.args[0];
                        String odex = (String) param.args[1];
                        Log.i(TAG, "load dex, input:" + dex + ", output:" + odex);
                    }
                });
    }

    static void testHook1() {
        DexposedBridge.hookAllConstructors(Thread.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Thread thread = (Thread) param.thisObject;
                Class<?> clazz = thread.getClass();
                if (clazz == Thread.class) {
                    /*Log.d(TAG, "found class extend Thread:" + clazz);
                    try {
                        DexposedBridge.findAndHookMethod(clazz, "run", new ThreadMethodHook());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }
                Log.d(TAG, "Thread: " + thread.getName() + " class:" + thread.getClass() + " is created.");
            }
        });
    }

    private static String TAG = "ThreadMethodHook";

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        //Thread t = (Thread) param.thisObject;
        //Log.i(TAG, "thread:" + t + ", started..");
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        //Thread t = (Thread) param.thisObject;
        //new RuntimeException(TAG).printStackTrace();
        //Log.i(TAG, "thread:" + param.thisObject + ", exit..");
    }
}
