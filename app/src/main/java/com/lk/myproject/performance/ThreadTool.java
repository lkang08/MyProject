package com.lk.myproject.performance;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;

class ThreadTool {
    private static String TAG;
    static ConcurrentHashMap<String, ThreadIssue> threadInfoMap;
    static ConcurrentHashMap<String, ThreadPoolIssue> threadPoolInfoMap;
    static ConcurrentHashMap<String, String> workerThreadPoolMap;

    ThreadTool() {
    }

    static void resetTag(String tag) {
        TAG = tag + "_ThreadTool";
    }

    static void init() {
        XLog.e(TAG, "init");
        hookWithEpic();
    }

    public static void hookWithEpic() {
        try {
            ThreadPoolExecutorConstructorHook constructorHook = new ThreadPoolExecutorConstructorHook();
            Constructor<?>[] constructors = ThreadPoolExecutor.class.getDeclaredConstructors();

            for (int i = 0; i < constructors.length; ++i) {
                if (constructors[i].getParameterTypes().length <= 6) {
                    DexposedBridge.hookMethod(constructors[i], constructorHook);
                }
            }

            DexposedBridge.hookAllConstructors(Thread.class, new ThreadConstructorHook());
            DexposedBridge.findAndHookMethod(Thread.class, "start", new Object[]{new ThreadStartHook()});
            DexposedBridge.findAndHookMethod(Thread.class, "run", new Object[]{new ThreadRunHook()});
            DexposedBridge.hookAllConstructors(Class.forName("java.util.concurrent.ThreadPoolExecutor$Worker"),
                    new WorkerConstructorHook());
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
            ThreadIssue threadIssues = (ThreadIssue) ThreadTool.threadInfoMap.get(threadKey);
            if (null == threadIssues) {
                XLog.e(ThreadTool.TAG, "can not find thread info when thread run !!!!!!");
            } else {
                ThreadTool.threadInfoMap.remove(threadKey);
                XLog.e(ThreadTool.TAG, "threadInfoMap size:" + ThreadTool.threadInfoMap.size());
                if (!TextUtils.isEmpty(threadIssues.threadPoolKey)) {
                    ThreadPoolIssue threadPoolIssues = (ThreadPoolIssue) ThreadTool.threadPoolInfoMap
                            .get(threadIssues.threadPoolKey);
                    if (null == threadPoolIssues) {
                        XLog.e(ThreadTool.TAG, "can not find thread pool info !!!!!!");
                    } else {
                        threadPoolIssues.removeThreadInfo(threadIssues);
                        if (threadPoolIssues.isEmpty()) {
                            ThreadTool.threadPoolInfoMap.remove(threadIssues.threadPoolKey);
                            XLog.e(ThreadTool.TAG, "threadPoolInfoMap size:" +
                                    ThreadTool.threadPoolInfoMap.size());
                        }

                    }
                }
            }
        }
    }

    static class ThreadStartHook extends XC_MethodHook {
        ThreadStartHook() {
        }

        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            String threadKey = Integer.toHexString(param.thisObject.hashCode());
            ThreadIssue threadIssues = (ThreadIssue) ThreadTool.threadInfoMap.get(threadKey);
            if (null == threadIssues) {
                XLog.e(ThreadTool.TAG, "can not find thread info when thread start !!!!!!");
                String threadName = "";
                if (param.thisObject instanceof Thread) {
                    threadName = " " + ((Thread) param.thisObject).getName();
                }
                threadIssues = new ThreadIssue("THREAD CREATE" + threadName);
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

        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            String threadName = "";
            if (param.thisObject instanceof Thread) {
                threadName = " " + ((Thread) param.thisObject).getName();
            }
            ThreadIssue threadIssues = new ThreadIssue("THREAD CREATE" + threadName);
            String threadKey = Integer.toHexString(param.thisObject.hashCode());
            threadIssues.key = threadKey;
            threadIssues.createTrace = StackTraceUtils.list();
            boolean hasRunnable = param.args.length == 1 && param.args[0] instanceof Runnable
                    || param.args.length > 1 && param.args[1] instanceof Runnable;
            String workerKey = "";
            if (hasRunnable) {
                Object runnable = param.args[0] instanceof Runnable ? param.args[0] : param.args[1];
                if ("java.util.concurrent.ThreadPoolExecutor$Worker".equals(
                        runnable.getClass().getName())) {
                    workerKey = Integer.toHexString(runnable.hashCode());
                }
            }

            if (ThreadTool.workerThreadPoolMap.containsKey(workerKey)) {
                String threadPoolKey = (String) ThreadTool.workerThreadPoolMap.get(workerKey);
                ThreadPoolIssue threadPoolIssues = (ThreadPoolIssue) ThreadTool.threadPoolInfoMap
                        .get(threadPoolKey);
                if (threadPoolIssues == null) {
                    threadPoolIssues = new ThreadPoolIssue("THREAD POOL CREATE");
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
                ThreadPoolIssue threadPoolIssues = new ThreadPoolIssue("THREAD POOL CREATE");
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
        List<ThreadIssue> childThreadList = new ArrayList();

        public ThreadPoolIssue(String msg) {
            super(3, msg, (Object) null);
        }

        protected void buildOtherString(StringBuilder sb) {
            if (!this.lostCreateTrace) {
                sb.append("create trace:\n");
            } else {
                sb.append("one thread create trace:\n");
            }

            this.buildListString(sb, this.createTrace);
        }

        void removeThreadInfo(ThreadIssue threadIssues) {
            synchronized (this) {
                this.childThreadList.remove(threadIssues);
            }
        }

        void addThreadInfo(ThreadIssue threadIssues) {
            synchronized (this) {
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
            super(3, msg, (Object) null);
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
