package com.lk.myproject.room.encrypt;

import androidx.room.TypeConverter;

public class StringConverter {
   @TypeConverter
   public static StringEntry revertStringConverter(String value) {
         return new StringEntry(value);
   }

   @TypeConverter
   public static String converterStringEntry(StringEntry entry) {
      return entry.getValue();
   }

}
