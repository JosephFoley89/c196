package com.task.c196.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.task.c196.Entity.Course;
import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course course);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Course course);

    @Delete
    void delete(Course course);

    //Queries
    @Query("SELECT * FROM courses ORDER BY id ASC")
    List<Course> getAllCourses();

    @Query ("SELECT * FROM courses WHERE termID= :termID ORDER BY id ASC")
    List<Course> getAllAssociatedCourses(int termID);

    @Query("SELECT COUNT(*) FROM courses WHERE id=:id")
    int isNewRecord(int id);

    @Query("SELECT COUNT(*) FROM courses")
    int getRowCount();

    @Query("DELETE FROM courses")
    void DELETEALL();

    @Query("SELECT * FROM courses where id=:id")
    Course getCourseByID(int id);
}
