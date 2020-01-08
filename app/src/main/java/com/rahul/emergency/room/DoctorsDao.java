package com.rahul.emergency.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DoctorsDao {

    @Query("select * from doctors")
    List<Doctors> getalldoctors();

    @Insert
    void insert(Doctors doctors);

    @Delete
    void delete(Doctors doctors);
}
