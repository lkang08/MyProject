package com.lk.myproject.performance;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import android.os.Handler;
import android.os.Looper;
import android.util.Printer;
import android.view.KeyEvent;

import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;

class UIBlockTool {
    private static String TAG;
    private static UIBlockTool.DumpInfoThread dumpInfoThread;
    private static Handler dumpInfoHandler;
    private static UIBlockTool.DumpInfoRunnable dumpMainThreadRunnable;

    UIBlockTool() {
    }

    static void resetTag(String tag) {
        TAG = tag + "_UIBlockTool";
    }

    static void start() {
        xLog.e(TAG, "start");
        startDumpInfoThread();
        hookDecorViewDispatchKeyEvent();
        initMainLooperPrinter();
    }

    private static void startDumpInfoThread() {
        dumpInfoThread.start();
    }

    private static void initMainLooperPrinter() {
        Looper.getMainLooper().setMessageLogging(new UIBlockTool.WatcherMainLooperPrinter());
    }

    private static void startDumpInfo() {
        if (null != dumpInfoHandler) {
            dumpInfoHandler.removeCallbacks(dumpMainThreadRunnable);
            dumpInfoHandler.postDelayed(dumpMainThreadRunnable, Config.UI_BLOCK_INTERVAL_TIME);
        }
    }

    private static void clearDumpInfo() {
        if (null != dumpInfoHandler) {
            dumpInfoHandler.removeCallbacks(dumpMainThreadRunnable);
        }
    }

    private static void hookDecorViewDispatchKeyEvent() {
        try {
            Class decorViewClass = Class.forName("com.android.internal.policy.DecorView");
            DexposedBridge.findAndHookMethod(decorViewClass, "dispatchKeyEvent", new Object[]{KeyEvent.class, new UIBlockTool.DecorViewDispatchKeyEventHook()});
        } catch (Exception var1) {
            var1.printStackTrace();
            xLog.e(TAG, "hookDecorViewDispatchKeyEvent", var1);
        }

    }

    static {
        TAG = PERF.TAG + "_UIBlockTool";
        dumpInfoThread = new UIBlockTool.DumpInfoThread("DumpInfoThread");
        dumpMainThreadRunnable = new UIBlockTool.DumpInfoRunnable();
    }

    static class DecorViewDispatchKeyEventHook extends XC_MethodHook {
        DecorViewDispatchKeyEventHook() {
        }

        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            UIBlockTool.startDumpInfo();
        }

        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            UIBlockTool.clearDumpInfo();
        }
    }

    private static class DumpInfoRunnable implements Runnable {
        private DumpInfoRunnable() {
        }

        public void run() {
            Issue uiIssue = new Issue(0, "UI BLOCK", StackTraceUtils.list(Looper.getMainLooper().getThread()));
            uiIssue.print();
        }
    }

    private static class DumpInfoThread extends Thread {
        DumpInfoThread(String name) {
            super(name);
        }

        public void run() {
            Looper.prepare();
            UIBlockTool.dumpInfoHandler = new Handler(Looper.myLooper());
            Looper.loop();
            super.run();
        }
    }

    private static class WatcherMainLooperPrinter implements Printer {
        private WatcherMainLooperPrinter() {
        }

        public void println(String x) {
            if (x != null && x.startsWith(">>>>>")) {
                UIBlockTool.startDumpInfo();
            } else if (x != null && x.startsWith("<<<<<")) {
                UIBlockTool.clearDumpInfo();
            }

        }
    }
}

