package com.lk.myproject.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    private static final String PATTERN_WITH_MINUTE_STR = "[0-9]{4}_[0-9]{2}_[0-9]{2}_[0-9]{2}_[0-9]{2}";
    private static Pattern PATTERN_WITH_MINUTE = Pattern.compile(PATTERN_WITH_MINUTE_STR);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm");

    public static void isTimeToday() {
        String time = "2020_08_12_15_06_00";
        String newT = findEndTimeFile(time);

        Date d = new Date();
        try {
            d = sdf.parse(newT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cur = Calendar.getInstance();
        Calendar old = Calendar.getInstance();
        old.setTime(d);
        if (cur.get(Calendar.YEAR) == old.get(Calendar.YEAR) &&
                cur.get(Calendar.MONTH) == old.get(Calendar.MONTH) &&
                cur.get(Calendar.DATE) == old.get(Calendar.DATE)) {
            String temp = "1";
        }
    }

    public static String findEndTimeFile(String fileName) {
        Matcher matcherMinute = PATTERN_WITH_MINUTE.matcher(fileName);
        if (matcherMinute.find()) {
            return matcherMinute.group();
        }
        return "";
    }
}
