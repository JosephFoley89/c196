package com.task.c196.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import com.task.c196.Entity.Instructor;

@Dao
public interface InstructorDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Instructor instructor);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Instructor instructor);

    @Delete
    void delete(Instructor instructor);

    //Queries
    @Query("SELECT * FROM instructors ORDER BY id ASC")
    List<Instructor> getAllInstructors();

    @Query("SELECT * FROM instructors WHERE courseId=:id")
    List<Instructor> getAllAssociatedInstructors(int id);

    @Query("SELECT * FROM instructors WHERE name=:name")
    List<Instructor> getInstructorByName(String name);

    @Query("SELECT COUNT(*) FROM terms WHERE id=:id")
    int isNewRecord(int id);

    @Query("DELETE FROM instructors")
    void DELETEALL();
}
