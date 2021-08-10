package com.lk.myproject.performance;

import android.content.Context;

import java.io.File;

public class PERF {
    static String TAG = "pTool";

    public PERF() {
    }

    public static void init(Builder builder) {
        if (builder == null) {
            builder = new Builder();
        }

        if (null == builder.issueSupplier) {
            throw new IllegalArgumentException("issue supplier is missing!!!");
        } else {
            xLog.setLogLevel(builder.logLevel);
            TAG = builder.globalTag;
            ThreadTool.resetTag(TAG);
            DumpTool.resetTag(TAG);
            FPSTool.resetTag(TAG);
            IPCTool.resetTag(TAG);
            UIBlockTool.resetTag(TAG);
            PerformanceHandler.resetTag(TAG);
            Issue.resetTag(TAG);
            Issue.init(builder.issueSupplier);
            if (builder.mCheckThread) {
                ThreadTool.init();
            }

            if (builder.mCheckUI) {
                Config.UI_BLOCK_INTERVAL_TIME = builder.mUIBlockIntervalTime;
                UIBlockTool.start();
            }

            if (builder.mCheckFPS) {
                FPSTool.start();
            }

            if (builder.mCheckIPC) {
                IPCTool.start();
            }

        }
    }

    public interface IssueSupplier {
        long maxCacheSize();

        File cacheRootDir();

        boolean upLoad(File var1);
    }

    public static class Builder {
        int logLevel = 3;
        boolean mCheckUI = false;
        long mUIBlockIntervalTime;
        boolean mCheckThread;
        boolean mCheckFPS;
        boolean mCheckIPC;
        IssueSupplier issueSupplier;
        /** @deprecated */
        @Deprecated
        Context appContext;
        String globalTag;

        public Builder() {
            this.mUIBlockIntervalTime = Config.UI_BLOCK_INTERVAL_TIME;
            this.mCheckThread = false;
            this.mCheckFPS = false;
            this.mCheckIPC = false;
            this.issueSupplier = null;
            this.appContext = null;
            this.globalTag = PERF.TAG;
        }

        public Builder checkUI(boolean check) {
            return this.checkUI(check, Config.UI_BLOCK_INTERVAL_TIME);
        }

        public Builder checkUI(boolean check, long blockIntervalTime) {
            this.mCheckUI = check;
            this.mUIBlockIntervalTime = blockIntervalTime;
            return this;
        }

        public Builder checkThread(boolean check) {
            this.mCheckThread = check;
            return this;
        }

        public Builder checkFps(boolean check) {
            this.mCheckFPS = check;
            return this;
        }

        public Builder checkIPC(boolean check) {
            this.mCheckIPC = check;
            return this;
        }

        public Builder globalTag(String tag) {
            this.globalTag = tag;
            return this;
        }

        public Builder issueSupplier(IssueSupplier supplier) {
            this.issueSupplier = supplier;
            return this;
        }

        public Builder logLevel(int level) {
            this.logLevel = level;
            return this;
        }

        public Builder build() {
            return this;
        }
    }
}
