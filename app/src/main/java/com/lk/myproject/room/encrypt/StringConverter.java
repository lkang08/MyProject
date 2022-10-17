package com.lk.myproject.room.encrypt;

import android.util.Log;

import androidx.room.TypeConverter;

public class StringConverter {
    @TypeConverter
    public static StringEntry revertStringConverter(String value) {
        Log.d("AndroidR", "revertStringConverter : " + value);
        String real = AccountEncrypt.getAccountEncrypt().getRealEncryptContent(value);
        Log.d("AndroidR", "revertStringConverter real: " +
                AccountEncrypt.getAccountEncrypt().decodeContent(real));
        return new StringEntry(AccountEncrypt.getAccountEncrypt().decodeContent(real));
    }

    @TypeConverter
    public static String converterStringEntry(StringEntry entry) {
        return AccountEncrypt.getAccountEncrypt().safeEncodeContent(entry.getValue(), true);
    }

}
