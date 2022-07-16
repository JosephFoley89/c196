package com.task.c196.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import com.task.c196.Entity.Term;

@Dao
public interface TermDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Term term);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Term term);

    @Delete
    void delete(Term term);

    //Queries
    @Query("Select * FROM terms ORDER BY id ASC")
    List<Term> getAllTerms();

    @Query("SELECT COUNT(*) FROM terms")
    int getRowCount();

    @Query("SELECT COUNT(*) FROM terms WHERE id=:id")
    int isNewRecord(int id);

    @Query("SELECT * FROM terms WHERE id=:id")
    Term getTermByID(int id);

    @Query("DELETE FROM terms")
    void DELETEALL();

}
