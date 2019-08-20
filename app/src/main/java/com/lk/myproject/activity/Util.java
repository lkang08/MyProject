package com.lk.myproject.activity;

import android.app.Activity;
import android.app.DialogFragment;

public class Util {
    public static void show(Activity activity, DialogFragment fragment) {
        fragment.show(activity.getFragmentManager(), "ttt");
    }
}
