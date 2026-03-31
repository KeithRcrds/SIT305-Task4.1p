package com.example.Task41p;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void insert(Event event);

    @Query("SELECT * FROM events ORDER BY date ASC")
    List<Event> getAllEvents();

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);


}
