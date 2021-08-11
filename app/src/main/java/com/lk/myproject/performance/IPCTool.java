package com.lk.myproject.performance;

import android.os.Binder;
import android.os.Parcel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;

class IPCTool {
    private static String TAG;
    private static TransactListenerHandler gTransactListenerHandler;

    IPCTool() {
    }

    static void resetTag(String tag) {
        TAG = tag + "_IPCTool";
    }

    static void start() {
        XLog.e(TAG, "start");
        hookWithEpic();
    }

    private static void hookWithEpic() {
        try {
            DexposedBridge.findAndHookMethod(Parcel.class, "readException",
                    new Object[]{new ParcelReadExceptionHook()});
            XLog.e(TAG, "hookWithEpic");
        } catch (Exception var1) {
            XLog.e(TAG, "hookWithEpic", var1);
        }

    }

    private static void hookTransactListener() {
        setTransactListener((Object) null);

        try {
            DexposedBridge.findAndHookMethod(Class.forName("android.os.BinderProxy"),
                    "setTransactListener",
                    new Object[]{Class.forName("android.os.Binder$ProxyTransactListener"),
                            new SetTransactListenerHook()});
            XLog.e(TAG, "hookTransactListener");
        } catch (Exception var1) {
            XLog.e(TAG, "hookTransactListener", var1);
        }

    }

    private static void setTransactListener(Object target) {
        try {
            Class binderProxy;
            if (null == gTransactListenerHandler) {
                binderProxy = IPCTool.class;
                synchronized (IPCTool.class) {
                    if (null == gTransactListenerHandler) {
                        gTransactListenerHandler = new TransactListenerHandler();
                    }
                }
            }

            binderProxy = Class.forName("android.os.BinderProxy");
            Class transactListener = Class.forName("android.os.Binder$ProxyTransactListener");
            Method setMethod = binderProxy.getDeclaredMethod("setTransactListener", transactListener);
            setMethod.setAccessible(true);
            gTransactListenerHandler.setTarget(target);
            Object proxyInstance = Proxy.newProxyInstance(Binder.class.getClassLoader(),
                    new Class[]{transactListener}, gTransactListenerHandler);
            setMethod.invoke((Object) null, proxyInstance);
            Field listener = binderProxy.getDeclaredField("sTransactListener");
            listener.setAccessible(true);
            XLog.e(TAG, "android.os.BinderProxy.sTransactListener is:" + listener.get((Object) null));
        } catch (Exception var7) {
            XLog.e(TAG, "setTransactListener error", var7);
        }

    }

    static {
        TAG = PERF.TAG + "_IPCTool";
        gTransactListenerHandler = null;
    }

    static class IPCIssue extends Issue {
        Object ipcInterface;

        public IPCIssue(Object ipcInterface, String msg, Object data) {
            super(2, msg, data);
            this.ipcInterface = ipcInterface;
        }

        protected void buildOtherString(StringBuilder sb) {
            sb.append("ipc interface: ").append(this.ipcInterface).append('\n');
        }
    }

    static class SetTransactListenerHook extends XC_MethodHook {
        SetTransactListenerHook() {
        }

        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            IPCTool.setTransactListener(param.args[0]);
        }
    }

    static class TransactListenerHandler implements InvocationHandler {
        private Object target;

        public TransactListenerHandler() {
        }

        public void setTarget(Object target) {
            this.target = target;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            if ("onTransactStarted".equals(methodName)) {
                Issue ipcIssue = new Issue(2, "IPC", StackTraceUtils.list());
                ipcIssue.print();
            }

            if (null != this.target) {
                method.setAccessible(true);
                return method.invoke(this.target, args);
            } else {
                return null;
            }
        }
    }

    static class ParcelReadExceptionHook extends XC_MethodHook {
        ParcelReadExceptionHook() {
        }

        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            Issue ipcIssue = new Issue(2, "IPC", StackTraceUtils.list());
            ipcIssue.print();
        }
    }

    static class ParcelWriteInterfaceTokenHook extends XC_MethodHook {
        ParcelWriteInterfaceTokenHook() {
        }

        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            XLog.e(IPCTool.TAG, "WriteInterfaceTokenHook:" + param.args[0]);
            IPCIssue ipcIssue = new IPCIssue(param.args[0], "IPC", StackTraceUtils.list());
            ipcIssue.print();
        }
    }

    static class BinderTransactProxyHook extends XC_MethodHook {
        BinderTransactProxyHook() {
        }

        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            Issue ipcIssue = new Issue(2, "IPC", StackTraceUtils.list());
            ipcIssue.print();
            super.beforeHookedMethod(param);
        }
    }
}