package com.lk.myproject.activity;

import android.app.Activity;
import android.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {
    public static void show(Activity activity, DialogFragment fragment) {
        fragment.show(activity.getFragmentManager(), "ttt");
    }

    public static void main(String[] main) {
        System.out.println("diff day:" + test());
    }

    public static int test() {
        long time = 1618384734000L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("diff day111:" + sdf.format(new Date(time)));
        int diff = (int) ((time - System.currentTimeMillis()) / 1000);
        int oneDay = 24 * 60 * 60;
        int diffDay = diff / oneDay;
        if (diff % oneDay > 0) {
            diffDay++;
        }
        return diffDay;
    }

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String getLastWeekInterval() {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) { //星期天
            dayOfWeek = 7;
        }
        int offset1 = 1 - dayOfWeek;
        int offset2 = 7 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 - 7);
        calendar2.add(Calendar.DATE, offset2 - 7);
        String lastBeginDate = sdf.format(calendar1.getTime());
        String lastEndDate = sdf.format(calendar2.getTime());
        return lastBeginDate + "," + lastEndDate;
    }

    public static String getThisWeekInterval() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        return imptimeBegin + "," + imptimeEnd;
    }

    public static String getLastMonthInterval() {
        Calendar c=Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-01");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String gtimelast = sdf1.format(c.getTime()); //上月
        System.out.println(gtimelast);
        int lastMonthMaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(lastMonthMaxDay);
        c.set(Calendar.DAY_OF_MONTH, lastMonthMaxDay);
        return gtimelast + "," + sdf2.format(c.getTime());
    }

    public static String getThisMonthInterval() {
        Calendar c=Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-01");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String gtimelast = sdf1.format(c.getTime()); //上月
        System.out.println(gtimelast);
        int lastMonthMaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(lastMonthMaxDay);
        c.set(Calendar.DAY_OF_MONTH, lastMonthMaxDay);
        return gtimelast + "," + sdf2.format(c.getTime());
    }
}
