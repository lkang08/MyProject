package com.lk.myproject.room;

import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "userId", name = "user_userId_idx", unique = true)})
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -555429051118770619L;
    public static final String USER_ID = "userId";
    @Nullable
    @PrimaryKey
    @ColumnInfo(name = "userId")
    private Long userId;

    @Nullable
    public Long getUserId() {
        return userId;
    }

    public void setUserId(@Nullable Long userId) {
        this.userId = userId;
    }
}
