package com.lk.myproject.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
   @Query("select * from user")
   List<User> getAll();

   @Query("select * from user where userid in (:ids)")
   List<User> loadAllByIds(String[] ids);

   @Query(("select * from user where nickname like :name"))
   User findByName(String name);

   @Insert
   void insertAll(User... users);

   @Delete
   void delete(User user);
}
