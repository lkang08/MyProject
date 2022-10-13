package com.lk.myproject.room;

import com.lk.myproject.room.encrypt.StringConverter;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

@Dao
@TypeConverters(StringConverter.class)
public interface HotLineDao {
 @Query("select * from hotline")
 List<HotLine> getAll();

 @Query("select * from hotline where id in (:ids)")
 List<HotLine> loadAllByIds(String[] ids);

 @Insert
 void insertAll(HotLine... hotLines);

 @Delete
 void delete(HotLine hotLine);
}
