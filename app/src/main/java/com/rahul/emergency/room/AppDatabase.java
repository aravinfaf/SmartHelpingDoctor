package com.rahul.emergency.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Doctors.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DoctorsDao doctorsDao();
}
