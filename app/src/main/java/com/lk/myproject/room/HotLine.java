package com.lk.myproject.room;

import com.lk.myproject.room.encrypt.StringConverter;

import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "hotline")
@TypeConverters(StringConverter.class)
public class HotLine implements Serializable {

    public static final String BELONG_USER_ID = "belongUserId";
    public static final String START_IMTE = "startTime";
    public static final String LIVE_ID = "liveId";

    @Nullable
    @PrimaryKey
    @ColumnInfo
    private Integer id;

    @Nullable
    public Integer getId() {
        return id;
    }

    public void setId(@Nullable Integer id) {
        this.id = id;
    }
}
