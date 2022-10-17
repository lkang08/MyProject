package com.lk.myproject.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, HotLine.class}, version = 82, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract HotLineDao hotLineDao();
}
