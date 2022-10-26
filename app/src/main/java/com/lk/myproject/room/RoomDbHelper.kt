package com.lk.myproject.room

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lk.myproject.MyApplication
import com.lk.myproject.ext.log

object RoomDbHelper {
    init {
        "RoomDbHelper".log()
    }

    private val MIGRATION_79_80 = object : Migration(79, 80) {
        override fun migrate(database: SupportSQLiteDatabase) {
        }
    }
    private val MIGRATION_80_81 = object : Migration(80, 81) {
        override fun migrate(database: SupportSQLiteDatabase) {
        }
    }
    private val MIGRATION_81_82 = object : Migration(81, 82) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE hotline "
                    + " ADD COLUMN encrypt TEXT"
            );
        }
    }
    var db = Room.databaseBuilder(
        MyApplication.app,
        AppDatabase::class.java, "bl_hujiao_db"
    ).addMigrations()
        .build()
}