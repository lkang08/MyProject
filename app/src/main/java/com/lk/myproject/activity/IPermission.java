package com.lk.myproject.activity;

import java.util.List;

public interface IPermission {
    /**
     * 同意权限
     * */
    void permissionGranted();

    /**
     * 拒绝权限并且选中不再提示
     * */
    void permissionNeverShow(int requestCode, List<String> denyList);

    /**
     * 取消权限
     * */
    void permissionCanceled(int requestCode, List<String> cancelList);
}
