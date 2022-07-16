package com.task.c196.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.task.c196.Entity.Assessment;
import com.task.c196.Entity.Course;

import java.util.List;

@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    //Queries
    @Query("SELECT * FROM assessments ORDER BY id ASC")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM assessments WHERE courseID= :courseID")
    List<Assessment> getAllAssociatedAssessments(int courseID);

    @Query("SELECT COUNT(*) FROM assessments WHERE id=:id")
    int isNewRecord(int id);

    @Query("DELETE FROM assessments")
    void DELETEALL();
}
