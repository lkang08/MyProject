package com.lk.myproject.room.encrypt;

import androidx.room.TypeConverter;

public class StringConverter {
    @TypeConverter
    public static StringEntry revertStringConverter(String value) {
        String real = AccountEncrypt.getAccountEncrypt().getRealEncryptContent(value);
        return new StringEntry(AccountEncrypt.getAccountEncrypt().decodeContent(real));
    }

    @TypeConverter
    public static String converterStringEntry(StringEntry entry) {
        return AccountEncrypt.getAccountEncrypt().safeEncodeContent(entry.getValue(), true);
    }

}
