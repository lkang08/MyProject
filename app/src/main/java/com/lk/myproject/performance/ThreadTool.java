package com.lk.myproject.performance;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import android.text.TextUtils;

import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

class ThreadTool {
    private static String TAG;
    static ConcurrentHashMap<String, ThreadTool.ThreadIssue> threadInfoMap;
    static ConcurrentHashMap<String, ThreadTool.ThreadPoolIssue> threadPoolInfoMap;
    static ConcurrentHashMap<String, String> workerThreadPoolMap;

    ThreadTool() {
    }

    static void resetTag(String tag) {
        TAG = tag + "_ThreadTool";
    }

    static void init() {
        xLog.e(TAG, "init");
        hookWithEpic();
    }

    public static void hookWithEpic() {
        try {
            ThreadTool.ThreadPoolExecutorConstructorHook constructorHook = new ThreadTool.ThreadPoolExecutorConstructorHook();
            Constructor<?>[] constructors = ThreadPoolExecutor.class.getDeclaredConstructors();

            for(int i = 0; i < constructors.length; ++i) {
                if (constructors[i].getParameterTypes().length <= 6) {
                    DexposedBridge.hookMethod(constructors[i], constructorHook);
                }
            }

            DexposedBridge.hookAllConstructors(Thread.class, new ThreadTool.ThreadConstructorHook());
            DexposedBridge.findAndHookMethod(Thread.class, "start", new Object[]{new ThreadTool.ThreadStartHook()});
            DexposedBridge.findAndHookMethod(Thread.class, "run", new Object[]{new ThreadTool.ThreadRunHook()});
            DexposedBridge.hookAllConstructors(Class.forName("java.util.concurrent.ThreadPoolExecutor$Worker"), new ThreadTool.WorkerConstructorHook());
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    static {
        TAG = PERF.TAG + "_ThreadTool";
        threadInfoMap = new ConcurrentHashMap(64);
        threadPoolInfoMap = new ConcurrentHashMap(32);
        workerThreadPoolMap = new ConcurrentHashMap(32);
    }

    static class ThreadRunHook extends XC_MethodHook {
        ThreadRunHook() {
        }

        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            String threadKey = Integer.toHexString(param.thisObject.hashCode());
            ThreadTool.ThreadIssue threadIssues = (ThreadTool.ThreadIssue) ThreadTool.threadInfoMap.get(threadKey);
            if (null == threadIssues) {
                xLog.e(ThreadTool.TAG, "can not find thread info when thread run !!!!!!");
            } else {
                ThreadTool.threadInfoMap.remove(threadKey);
                xLog.e(ThreadTool.TAG, "threadInfoMap size:" + ThreadTool.threadInfoMap.size());
                if (!TextUtils.isEmpty(threadIssues.threadPoolKey)) {
                    ThreadTool.ThreadPoolIssue threadPoolIssues = (ThreadTool.ThreadPoolIssue) ThreadTool.threadPoolInfoMap.get(threadIssues.threadPoolKey);
                    if (null == threadPoolIssues) {
                        xLog.e(ThreadTool.TAG, "can not find thread pool info !!!!!!");
                    } else {
                        threadPoolIssues.removeThreadInfo(threadIssues);
                        if (threadPoolIssues.isEmpty()) {
                            ThreadTool.threadPoolInfoMap.remove(threadIssues.threadPoolKey);
                            xLog.e(ThreadTool.TAG, "threadPoolInfoMap size:" + ThreadTool.threadPoolInfoMap.size());
                        }

                    }
                }
            }
        }
    }

    static class ThreadStartHook extends XC_MethodHook {
        ThreadStartHook() {
        }

        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            String threadKey = Integer.toHexString(param.thisObject.hashCode());
            ThreadTool.ThreadIssue threadIssues = (ThreadTool.ThreadIssue) ThreadTool.threadInfoMap.get(threadKey);
            if (null == threadIssues) {
                xLog.e(ThreadTool.TAG, "can not find thread info when thread start !!!!!!");
                threadIssues = new ThreadTool.ThreadIssue("THREAD CREATE");
                threadIssues.key = threadKey;
                threadIssues.lostCreateTrace = true;
                ThreadTool.threadInfoMap.put(threadKey, threadIssues);
            }

            if (TextUtils.isEmpty(threadIssues.threadPoolKey)) {
                threadIssues.startTrace = StackTraceUtils.list();
                threadIssues.print();
            }

        }
    }

    static class ThreadConstructorHook extends XC_MethodHook {
        ThreadConstructorHook() {
        }

        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            ThreadTool.ThreadIssue threadIssues = new ThreadTool.ThreadIssue("THREAD CREATE");
            String threadKey = Integer.toHexString(param.thisObject.hashCode());
            threadIssues.key = threadKey;
            threadIssues.createTrace = StackTraceUtils.list();
            boolean hasRunnable = param.args.length == 1 && param.args[0] instanceof Runnable || param.args.length > 1 && param.args[1] instanceof Runnable;
            String workerKey = "";
            if (hasRunnable) {
                Object runnable = param.args[0] instanceof Runnable ? param.args[0] : param.args[1];
                if ("java.util.concurrent.ThreadPoolExecutor$Worker".equals(runnable.getClass().getName())) {
                    workerKey = Integer.toHexString(runnable.hashCode());
                }
            }

            if (ThreadTool.workerThreadPoolMap.containsKey(workerKey)) {
                String threadPoolKey = (String) ThreadTool.workerThreadPoolMap.get(workerKey);
                ThreadTool.ThreadPoolIssue threadPoolIssues = (ThreadTool.ThreadPoolIssue) ThreadTool.threadPoolInfoMap.get(threadPoolKey);
                if (threadPoolIssues == null) {
                    threadPoolIssues = new ThreadTool.ThreadPoolIssue("THREAD POOL CREATE");
                    threadPoolIssues.key = threadPoolKey;
                    threadPoolIssues.lostCreateTrace = true;
                    threadPoolIssues.createTrace = StackTraceUtils.list();
                    ThreadTool.threadPoolInfoMap.put(threadPoolKey, threadPoolIssues);
                    threadPoolIssues.print();
                }

                threadIssues.threadPoolKey = threadPoolKey;
                threadPoolIssues.addThreadInfo(threadIssues);
                ThreadTool.workerThreadPoolMap.remove(workerKey);
            }

            ThreadTool.threadInfoMap.put(threadKey, threadIssues);
        }
    }

    static class WorkerConstructorHook extends XC_MethodHook {
        WorkerConstructorHook() {
        }

        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            String workerKey = Integer.toHexString(param.thisObject.hashCode());
            String threadPoolKey = Integer.toHexString(param.args[0].hashCode());
            ThreadTool.workerThreadPoolMap.put(workerKey, threadPoolKey);
        }
    }

    static class ThreadPoolExecutorConstructorHook extends XC_MethodHook {
        ThreadPoolExecutorConstructorHook() {
        }

        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            String threadPoolInfoKey = Integer.toHexString(param.thisObject.hashCode());
            if (!ThreadTool.threadPoolInfoMap.containsKey(threadPoolInfoKey)) {
                ThreadTool.ThreadPoolIssue threadPoolIssues = new ThreadTool.ThreadPoolIssue("THREAD POOL CREATE");
                threadPoolIssues.key = threadPoolInfoKey;
                threadPoolIssues.createTrace = StackTraceUtils.list();
                ThreadTool.threadPoolInfoMap.put(threadPoolInfoKey, threadPoolIssues);
                threadPoolIssues.print();
            }
        }
    }

    static class ThreadPoolIssue extends Issue {
        String key;
        boolean lostCreateTrace = false;
        List<String> createTrace;
        List<ThreadTool.ThreadIssue> childThreadList = new ArrayList();

        public ThreadPoolIssue(String msg) {
            super(3, msg, (Object)null);
        }

        protected void buildOtherString(StringBuilder sb) {
            if (!this.lostCreateTrace) {
                sb.append("create trace:\n");
            } else {
                sb.append("one thread create trace:\n");
            }

            this.buildListString(sb, this.createTrace);
        }

        void removeThreadInfo(ThreadTool.ThreadIssue threadIssues) {
            synchronized(this) {
                this.childThreadList.remove(threadIssues);
            }
        }

        void addThreadInfo(ThreadTool.ThreadIssue threadIssues) {
            synchronized(this) {
                this.childThreadList.add(threadIssues);
            }
        }

        boolean isEmpty() {
            return this.childThreadList.isEmpty();
        }
    }

    static class ThreadIssue extends Issue {
        String key;
        String threadPoolKey;
        boolean lostCreateTrace = false;
        List<String> createTrace;
        List<String> startTrace;

        public ThreadIssue(String msg) {
            super(3, msg, (Object)null);
        }

        protected void buildOtherString(StringBuilder sb) {
            if (!this.lostCreateTrace) {
                sb.append("create trace:\n");
                this.buildListString(sb, this.createTrace);
            }

            sb.append("start trace:\n");
            this.buildListString(sb, this.startTrace);
        }
    }
}
