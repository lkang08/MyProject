package com.lk.myproject.performance;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import java.util.HashMap;

public class PerformanceHandler extends Handler {
    private static String TAG;
    private static HashMap<String, PerformanceHandler.HandlerIssue> msgIssuesMap;

    public PerformanceHandler() {
    }

    static void resetTag(String tag) {
        TAG = tag + "_PerformanceHandler";
    }

    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        boolean result = super.sendMessageAtTime(msg, uptimeMillis);
        if (result) {
            String msgKey = Integer.toHexString(msg.hashCode());
            PerformanceHandler.HandlerIssue msgIssues = new PerformanceHandler.HandlerIssue("HANDLER SEND MESSAGE", StackTraceUtils.list());
            msgIssuesMap.put(msgKey, msgIssues);
        }

        return result;
    }

    public void dispatchMessage(Message msg) {
        String msgKey = Integer.toHexString(msg.hashCode());
        long startTime = SystemClock.elapsedRealtime();
        super.dispatchMessage(msg);
        long costTime = SystemClock.elapsedRealtime() - startTime;
        if (costTime > Config.MAX_HANDLER_DISPATCH_MSG_TIME && msgIssuesMap.containsKey(msgKey)) {
            PerformanceHandler.HandlerIssue msgIssues = (PerformanceHandler.HandlerIssue)msgIssuesMap.get(msgKey);
            msgIssues.costTime = costTime;
            msgIssues.print();
        }

        msgIssuesMap.remove(msgKey);
    }

    static {
        TAG = PERF.TAG + "_PerformanceHandler";
        msgIssuesMap = new HashMap();
    }

    static class HandlerIssue extends Issue {
        protected long costTime;

        public HandlerIssue(String msg, Object data) {
            super(4, msg, data);
        }

        protected void buildOtherString(StringBuilder sb) {
            sb.append("cost time: ").append(this.costTime).append(" ms").append('\n');
        }
    }
}

