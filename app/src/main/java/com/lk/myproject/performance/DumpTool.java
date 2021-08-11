package com.lk.myproject.performance;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

import java.io.FileDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;

class DumpTool {
    private static String TAG;
    private static LinkedHashSet<DumpSysListener> dumpListeners;

    DumpTool() {
    }

    public static void addDumpListener(DumpSysListener dumpListener) {
        dumpListeners.add(dumpListener);
    }

    public static void removeDumpListener(DumpSysListener dumpListener) {
        dumpListeners.remove(dumpListener);
    }

    static void resetTag(String tag) {
        TAG = tag + "_DumpTool";
    }

    static void init(String serviceName) {
        XLog.e(TAG, "init");

        try {
            addService(serviceName);
        } catch (Exception var2) {
            XLog.w(TAG, "DumpTool init error:", var2);
        }

    }

    private static void addService(String serviceName) throws NoSuchMethodException,
            ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class<?> smClass = Class.forName("android.os.ServiceManager");
        Method addServiceMethod = smClass.getDeclaredMethod("addService", String.class, IBinder.class);
        addServiceMethod.setAccessible(true);
        addServiceMethod.invoke((Object) null, serviceName, new DumpSysBinder());
    }

    static {
        TAG = PERF.TAG + "_DumpTool";
        dumpListeners = new LinkedHashSet();
    }

    public interface DumpSysListener {
        boolean dump(String[] var1);
    }

    static class DumpSysBinder extends Binder implements IInterface {
        DumpSysBinder() {
        }

        public void dump(FileDescriptor fd, String[] args) {
            super.dump(fd, args);
            XLog.e(DumpTool.TAG, "dump");
            this.dump(args);
        }

        public void dumpAsync(FileDescriptor fd, String[] args) {
            super.dumpAsync(fd, args);
            XLog.e(DumpTool.TAG, "dumpAsync");
            this.dump(args);
        }

        private void dump(String[] args) {
            XLog.e(DumpTool.TAG, Arrays.toString(args));
            Iterator var2 = DumpTool.dumpListeners.iterator();

            while (var2.hasNext()) {
                DumpSysListener listener = (DumpSysListener) var2.next();
                if (listener.dump(args)) {
                    break;
                }
            }

        }

        public IBinder asBinder() {
            return this;
        }
    }
}

