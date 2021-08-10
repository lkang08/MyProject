package com.lk.myproject.performance;

import de.robv.android.xposed.DexposedBridge;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.weishu.epic.BuildConfig;

class StackTraceUtils {
    /** @deprecated */
    @Deprecated
    static Set<String> ignorePackageSet = new HashSet();

    StackTraceUtils() {
    }

    public static List<String> list() {
        StackTraceElement[] stackTraceElements = (new Throwable()).getStackTrace();
        return list(stackTraceElements);
    }

    public static List<String> list(Thread thread) {
        StackTraceElement[] stackTraceElements = thread.getStackTrace();
        return list(stackTraceElements);
    }

    public static List<String> list(StackTraceElement[] stackTraceElements) {
        List<String> list = new ArrayList(stackTraceElements.length);
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;

        for(int len = stackTraceElements.length; i < len; ++i) {
            StackTraceElement element = stackTraceElements[i];
            list.add(stringStackTraceElement(element, stringBuilder));
        }

        return list;
    }

    /** @deprecated */
    @Deprecated
    private static String filterPackageName(String className) {
        return filterPackageName(new StringBuilder(), className);
    }

    /** @deprecated */
    @Deprecated
    private static String filterPackageName(StringBuilder stringBuilder, String className) {
        stringBuilder.setLength(0);
        int count = 0;
        int m = 0;

        for(int n = className.length(); m < n; ++m) {
            char c = className.charAt(m);
            if (c == '.') {
                ++count;
            }

            if (count == 3) {
                break;
            }

            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }

    private static String stringStackTraceElement(StackTraceElement element, StringBuilder stringBuilder) {
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(element.getClassName()).append('.').append(element.getMethodName()).append('(').append(element.getFileName()).append(':').append(element.getLineNumber()).append(')');
        return stringBuilder.toString();
    }

    static {
        StringBuilder stringBuilder = new StringBuilder();
        ignorePackageSet.add(filterPackageName(stringBuilder, StackTraceUtils.class.getName()));
        ignorePackageSet.add(filterPackageName(stringBuilder, BuildConfig.class.getName()));
        ignorePackageSet.add(filterPackageName(stringBuilder, DexposedBridge.class.getName()));
    }
}
