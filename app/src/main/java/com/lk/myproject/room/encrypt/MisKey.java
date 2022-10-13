package com.lk.myproject.room.encrypt;


import java.util.HashSet;
import java.util.Set;

public class MisKey {
    private static final String ACCOUNT_INFO = "AccountInfo_UDB";
    private static final String TAG = "MisKey";
    private static final String ENCRYPT_KEY = "MNBVCXZLKJHGFDSA";
    private static final Set<Integer> CACHE_STATE = new HashSet<>();
    private static String encryptString =
            "4B243055CAF4B7ECA410A51AA669867F2CF0EE908EC2461914070DBBC95A43095F489BE68DBF7D9C362296F4763728DF";
    private static String encryptKey1 = "QWERTYUIOPASDFGH";
    private static String encryptString1 =
            "A2DB5C7D51101BFE04921F812D3DE82A6203F7E2728D2BE35FC6C98A845673FE1459405B789A1A07AC474E62AB24EEE2";

    public static String getUpKeySting() throws Exception {
        return AesUtils.aesDecrypt(AesUtils.aesDecrypt(encryptString, ENCRYPT_KEY), encryptKey1);
    }

    public static String getDownKeySting() throws Exception {
        return AesUtils.aesDecrypt(AesUtils.aesDecrypt(encryptString1, encryptKey1), ENCRYPT_KEY);
    }

    /**
     * 升级用户 默认为勾选
     *
     * @return
     */
    public static boolean isPrivacyCbChecked(int key) {
        return CACHE_STATE.contains(key);
    }

    public static void updatePrivacyMemCache(boolean check, int key) {
        if (!check) {
            CACHE_STATE.remove(key);
        } else {
            CACHE_STATE.add(key);
        }
        if (key == -1) {
            CACHE_STATE.clear();
        }
    }
}