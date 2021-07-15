package com.lk.myproject.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lk.myproject.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * @Date Created: 2019/4/15
 * @Author: Jiangxq
 * @Description: 一个透明的activity 用来申请所需要权限
 */

public class PermissionRequestActivity extends Activity {

    private static IPermission permissionListener;
    private String[] permissions;
    private String[] dialogContens;
    private boolean showDialog = false;
    private static final String PERMISSION_KEY = "permission_key";
    private static final String REQUEST_CODE = "request_code";
    private static final String SHOW_DIALOG = "show_dialog";
    private static final String DIALOG_CONTENT = "dialog_content";
    private int requestCode;
    public static final String TAG = "lk###";

    /**
     * 跳转到Activity申请权限
     *
     * @param context       Context
     * @param permissions   Permission List
     * @param dialogContent 对话框内容
     * @param showDialog    权限申请前是否先展示对话框
     * @param iPermission   Interface
     */
    public static void permissionRequest(Context context, String[] permissions, String[] dialogContent,
                                         boolean showDialog,
                                         int requestCode, IPermission iPermission) {
        permissionListener = iPermission;
        Intent intent = new Intent(context, PermissionRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArray(PERMISSION_KEY, permissions);
        bundle.putStringArray(DIALOG_CONTENT, dialogContent);
        bundle.putInt(REQUEST_CODE, requestCode);
        bundle.putBoolean(SHOW_DIALOG, showDialog);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    private void showPermissionDialog(String[] permissions, String[] dialogContents) {
        /*if (!PermissionUtils.hasSelfPermissions(this, permissions)) {
            getDialogManager().showOkCancleCancelBigTips(dialogContents[0],
                    dialogContents[1], dialogContents[3],
                    Color.parseColor("#FAC200"), dialogContents[2], 0, true,
                    new DialogManager.OkCancelDialogListener() {
                        @Override
                        public void onCancel() {
                            dismissDialog();
                            if (permissionListener != null) {
                                permissionListener.permissionCanceled(requestCode, Arrays.asList(permissions));
                                permissionListener = null;
                            }
                            finish();
                        }
                        @Override
                        public void onOk() {
                            requestPermission(permissions);
                        }
                    }
            );
            getDialogManager().setCanceledOnClickOutside(false);
            getDialogManager().getDialog().setCancelable(false);
        }*/
    }

    public void dismissDialog() {
        if (checkActivityValid() && checkActivityValid()) {
            //getDialogManager().dismissDialog();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @TargetApi(17)
    public boolean checkActivityValid() {
        if (isFinishing()) {
            return false;
        }
        return !isDestroyed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);
        //mDialogManager = new DialogManager(this);
        if (getIntent() != null) {
            initData(getIntent());
        }
    }

    private void initData(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            permissions = bundle.getStringArray(PERMISSION_KEY);
            requestCode = bundle.getInt(REQUEST_CODE, 0);
            showDialog = bundle.getBoolean(SHOW_DIALOG, false);
            dialogContens = bundle.getStringArray(DIALOG_CONTENT);
        }
        if (permissions == null || permissions.length <= 0) {
            finish();
            return;
        }
        if (showDialog) {
            showPermissionDialog(permissions, dialogContens);
        } else {
            requestPermission(permissions);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData(intent);
    }

    /**
     * 申请权限
     *
     * @param permissions permission list
     */
    private void requestPermission(String[] permissions) {
        Log.d(TAG, "request permission, permission list: " + Arrays.toString(permissions));
        if (PermissionUtils.hasSelfPermissions(this, permissions)) {
            //all permissions granted
            if (permissionListener != null) {
                permissionListener.permissionGranted();
                permissionListener = null;
            }
            finish();
        } else {
            //request permissions
            Log.d(TAG, "&&&& requestPermissions");
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult grantResults: " + Arrays.toString(grantResults));
        if (PermissionUtils.verifyPermissions(grantResults)) {
            //所有权限都同意
            if (permissionListener != null) {
                permissionListener.permissionGranted();
            }
        } else {
            if (permissions.length != grantResults.length) {
                return;
            }
            List<String> failedPermissionList = getFailedPermissions(grantResults);
            if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {
                if (permissionListener != null) {
                    permissionListener.permissionNeverShow(requestCode, failedPermissionList);
                }
            } else {
                //权限被取消
                if (permissionListener != null) {
                    permissionListener.permissionCanceled(requestCode, failedPermissionList);
                }
            }
        }
        permissionListener = null;
        finish();
    }

    private List<String> getFailedPermissions(int[] grantResults) {
        List<String> failedPermissionList = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == -1) {
                failedPermissionList.add(permissions[i]);
            }
        }
        return failedPermissionList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //getDialogManager().dismissDialog();
    }
}
