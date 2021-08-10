package com.lk.myproject.performance;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;

class FPSTool {
    private static String TAG;
    private static FPSTool.FrameRunnable frameRunnable;
    private static Handler handler;

    FPSTool() {
    }

    static void resetTag(String tag) {
        TAG = tag + "_FPSTool";
    }

    static void start() {
        xLog.e(TAG, "start");
        handler = new Handler(Looper.getMainLooper());
        handler.post(frameRunnable);
        Choreographer.getInstance().postFrameCallback(frameRunnable);
    }

    static {
        TAG = PERF.TAG + "_FPSTool";
        frameRunnable = new FPSTool.FrameRunnable();
        handler = new Handler();
    }

    private static class FrameRunnable implements Runnable, FrameCallback {
        long time;
        int count;

        private FrameRunnable() {
            this.time = 0L;
            this.count = 0;
        }

        public void doFrame(long frameTimeNanos) {
            ++this.count;
            Choreographer.getInstance().postFrameCallback(this);
        }

        public void run() {
            long curTime = SystemClock.elapsedRealtime();
            if (this.time != 0L) {
                int fps = (int)(1000.0F * (float)this.count / (float)(curTime - this.time) + 0.5F);
                String fpsStr = String.format("APP FPS is: %-3sHz", fps);
                if (fps <= 50) {
                    xLog.e(FPSTool.TAG, fpsStr);
                } else {
                    xLog.w(FPSTool.TAG, fpsStr);
                }
            }

            this.count = 0;
            this.time = curTime;
            FPSTool.handler.postDelayed(this, Config.FPS_INTERVAL_TIME);
        }
    }
}

