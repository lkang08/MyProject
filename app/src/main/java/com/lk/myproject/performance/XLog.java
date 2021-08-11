package com.lk.myproject.performance;

import android.util.Log;

class XLog {
    private static int LOG_LEVEL = 3;

    XLog() {
    }

    public static void setLogLevel(int logLevel) {
        LOG_LEVEL = logLevel;
    }

    public static void dft(String tag, String msg, Object... args) {
        if (LOG_LEVEL <= 3) {
            d(tag, String.format(msg, args));
        }

    }

    public static void d(String tag, String msg) {
        if (LOG_LEVEL <= 3) {
            Log.d(tag, msg);
        }

    }

    public static void ift(String tag, String msg, Object... args) {
        if (LOG_LEVEL <= 4) {
            i(tag, String.format(msg, args));
        }

    }

    public static void i(String tag, String msg) {
        if (LOG_LEVEL <= 4) {
            Log.i(tag, msg);
        }

    }

    public static void wft(String tag, String msg, Object... args) {
        if (LOG_LEVEL <= 5) {
            w(tag, String.format(msg, args));
        }

    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL <= 5) {
            Log.w(tag, msg);
        }

    }

    public static void w(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= 5) {
            Log.w(tag, msg, tr);
        }

    }

    public static void eft(String tag, String msg, Object... args) {
        if (LOG_LEVEL <= 6) {
            e(tag, String.format(msg, args));
        }

    }

    public static void e(String tag, String msg) {
        if (LOG_LEVEL <= 6) {
            Log.e(tag, msg);
        }

    }

    public static void e(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= 6) {
            Log.e(tag, msg, tr);
        }

    }
}
