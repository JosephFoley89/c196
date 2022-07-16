package com.task.c196.Database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.task.c196.Entity.*;
import com.task.c196.DAO.*;
import com.task.c196.Utility.Converter;

@Database(entities = {Assessment.class, Course.class, Instructor.class, Term.class}, version=3, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class db extends RoomDatabase {
    public abstract AssessmentDAO assessmentDAO();
    public abstract CourseDAO courseDAO();
    public abstract InstructorDAO instructorDAO();
    public abstract TermDAO termDAO();

    private static final int NUM_THREADS = 4;

    static final ExecutorService dbWriteExecutor = Executors.newFixedThreadPool(NUM_THREADS);

    public static volatile db INSTANCE;

    static db getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (db.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder( context.getApplicationContext(), db.class, "c196.db")
                        .fallbackToDestructiveMigration()
                        .addCallback(defaultConditions)
                        .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback defaultConditions = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
        super.onOpen(db);
        dbWriteExecutor.execute(() -> {
            /*

            [this was used for testing data. Keeping the instructors added since the users are unable to add instructors themselves]

            AssessmentDAO mAssessmentDAO = INSTANCE.assessmentDAO();
            CourseDAO mCourseDAO = INSTANCE.courseDAO();
            InstructorDAO mInstructorDAO = INSTANCE.instructorDAO();
            TermDAO mTermDAO = INSTANCE.termDAO();

            Assessment a1 = new Assessment(1, "Threading the Needle", new Date("11/11/2021"), "Objective", 1);
            Assessment a2 = new Assessment(2, "Sales", new Date("11/12/2021"), "Performance", 2);
            Assessment a3 = new Assessment(3, "Stage Show", new Date("11/13/2021"), "Objective", 3);
            Assessment a4 = new Assessment(4, "Threading the Needle pt 2", new Date("11/14/2021"), "Performance", 4);

            Instructor i1 = new Instructor(1, "Martha Stewart", "8675309", "InsideTrade@gmail.com", 1);
            Instructor i2 = new Instructor(2, "Tony Montana", "8675310", "SayHelloToMyLilEmail@outlook.com", 2);
            Instructor i3 = new Instructor(3, "Randy Quaid", "8675311", "QuaidBaby@aol.com", 3);
            Instructor i4 = new Instructor(4, "Peyton Manning", "8675312", "FiveHead@hotmail.com", 4);

            Course c1 = new Course(1, "Home Economics", new Date("11/11/21"), new Date("12/11/21"), "In-Progress", "You say that you made a big mistake.", 1);
            Course c2 = new Course(2, "Advanced Entrepreneurship", new Date("12/12/21"), new Date("1/12/22"), "Inactive", "You say that you made a big mistake.", 1);
            Course c3 = new Course(3, "Theater", new Date("1/13/22"), new Date("2/14/22"), "Inactive", "You say that you made a big mistake.", 1);
            Course c4 = new Course(4, "Manning Passing Academy", new Date("2/15/22"), new Date("3/16/22"), "Inactive", "You say that you made a big mistake.", 1);

            Term term = new Term(1, "First Term", new Date("11/11/21"), new Date("5/11/22"));

            mAssessmentDAO.insert(a1);
            mAssessmentDAO.insert(a2);
            mAssessmentDAO.insert(a3);
            mAssessmentDAO.insert(a4);

            mInstructorDAO.insert(i1);
            mInstructorDAO.insert(i2);
            mInstructorDAO.insert(i3);
            mInstructorDAO.insert(i4);

            mCourseDAO.insert(c1);
            mCourseDAO.insert(c2);
            mCourseDAO.insert(c3);
            mCourseDAO.insert(c4);

            mTermDAO.insert(term);*/

            //Adding instructors since the user is unable to.

            InstructorDAO mInstructorDAO = INSTANCE.instructorDAO();

            Instructor i1 = new Instructor(1, "Guybrush Threepwood", "8675309", "SecretOf@Gmail.com", 1);
            Instructor i2 = new Instructor(2, "Alan Wake", "8675310", "AWake@Microsoft.com", 1);
            Instructor i3 = new Instructor(3, "Laura Croft", "8675311", "LauraCroft@AOL.com", 3);
            Instructor i4 = new Instructor(4, "Geralt Rivia", "8675312", "Geralt@Rivia.com", 4);
            Instructor i5 = new Instructor(4, "Geralt Rivia", "8675312", "Geralt@Rivia.com", 3);

            mInstructorDAO.insert(i1);
            mInstructorDAO.insert(i2);
            mInstructorDAO.insert(i3);
            mInstructorDAO.insert(i4);
            mInstructorDAO.insert(i5);

        });
        }
    };
}
