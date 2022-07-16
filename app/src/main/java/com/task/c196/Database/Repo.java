package com.task.c196.Database;

import android.app.Application;
import com.task.c196.DAO.*;
import com.task.c196.Entity.*;
import java.util.List;

public class Repo {
    private AssessmentDAO mAssessmentDAO;
    private CourseDAO mCourseDAO;
    private InstructorDAO mInstructorDAO;
    private TermDAO mTermDAO;

    private List<Assessment> mAllAssessments;
    private List<Course> mAllCourses;
    private List<Instructor> mAllInstructors;
    private List<Term> mAllTerms;

    private boolean isNewRecord;
    private String rowCountString;

    private Instructor instructor;
    private Term term;
    private Course course;

    public Repo(Application application) {
        db DB = db.getDatabase(application);
        mAssessmentDAO = DB.assessmentDAO();
        mCourseDAO = DB.courseDAO();
        mInstructorDAO = DB.instructorDAO();
        mTermDAO = DB.termDAO();

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**Getting all from tables*/
    public List<Assessment> getAllAssessments() {
        db.dbWriteExecutor.execute(() -> {
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mAllAssessments;
    }

    public List<Course> getAllCourses() {
        db.dbWriteExecutor.execute(() -> {
            mAllCourses = mCourseDAO.getAllCourses();
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mAllCourses;
    }

    public List<Instructor> getAllInstructors() {
        db.dbWriteExecutor.execute(() -> {
            mAllInstructors = mInstructorDAO.getAllInstructors();
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mAllInstructors;
    }

    public List<Term> getAllTerms() {
        db.dbWriteExecutor.execute(() -> {
            mAllTerms = mTermDAO.getAllTerms();
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mAllTerms;
    }

    /**Inserts*/
    public void insert(Assessment a) {
        db.dbWriteExecutor.execute(() -> {
            mAssessmentDAO.insert(a);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void insert(Course c) {
        db.dbWriteExecutor.execute(() -> {
            mCourseDAO.insert(c);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void insert(Instructor i) {
        db.dbWriteExecutor.execute(() -> {
            mInstructorDAO.insert(i);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void insert(Term t) {
        db.dbWriteExecutor.execute(() -> {
            mTermDAO.insert(t);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**Updates*/
    public void update(Assessment a) {
        db.dbWriteExecutor.execute(() -> {
            mAssessmentDAO.update(a);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update(Course c) {
        db.dbWriteExecutor.execute(() -> {
            mCourseDAO.update(c);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update(Instructor i) {
        db.dbWriteExecutor.execute(() -> {
            mInstructorDAO.update(i);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update(Term t) {
        db.dbWriteExecutor.execute(() -> {
            mTermDAO.update(t);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**Deletes*/
    public void delete(Assessment a) {
        db.dbWriteExecutor.execute(() -> {
            mAssessmentDAO.delete(a);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Course c) {
        db.dbWriteExecutor.execute(() -> {
            mCourseDAO.delete(c);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Instructor i) {
        db.dbWriteExecutor.execute(() -> {
            mInstructorDAO.delete(i);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Term t) {
        db.dbWriteExecutor.execute(() -> {
            mTermDAO.delete(t);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getRowCount(String table) {
        db.dbWriteExecutor.execute(() -> {
            if (table == "term") {
                int count = mTermDAO.getRowCount()+1;
                rowCountString = String.valueOf(count);
            } else if (table == "course") {
                int count = mCourseDAO.getRowCount()+1;
                rowCountString = String.valueOf(count);
            }
        });
        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rowCountString;
    }

    public List<Course> getAssociatedCourses(String term) {
        db.dbWriteExecutor.execute(() -> {
            mAllCourses = mCourseDAO.getAllAssociatedCourses(Integer.parseInt(term));
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mAllCourses;
    }

    public List<Assessment> getAssociatedAssessments(String assessment) {
        db.dbWriteExecutor.execute(() -> {
            mAllAssessments = mAssessmentDAO.getAllAssociatedAssessments(Integer.parseInt(assessment));
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return mAllAssessments;
    }

    public boolean isNewRecord(String table, int id) {
        db.dbWriteExecutor.execute(() -> {
            if (table == "term") {
                if (mTermDAO.isNewRecord(id) == 0) {
                    isNewRecord = true;
                } else {
                    isNewRecord = false;
                }
            } else if (table == "course") {
                if (mCourseDAO.isNewRecord(id) == 0) {
                    isNewRecord = true;
                } else {
                    isNewRecord = false;
                }
            } else if (table == "assessment") {
                if (mAssessmentDAO.isNewRecord(id) == 0) {
                    isNewRecord = true;
                } else {
                    isNewRecord = false;
                }
            } else if (table == "instructor") {
                if (mInstructorDAO.isNewRecord(id) == 0) {
                    isNewRecord = true;
                } else {
                    isNewRecord = false;
                }
            }
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isNewRecord;
    }

    public List<Instructor> getAssociatedInstructors(String instructor) {
        db.dbWriteExecutor.execute(() -> {
            mAllInstructors = mInstructorDAO.getAllAssociatedInstructors(Integer.parseInt(instructor));
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mAllInstructors;
    }

    public int getAssociatedAssessments(int id) {
        db.dbWriteExecutor.execute(() -> {
            mAllAssessments = mAssessmentDAO.getAllAssociatedAssessments(id);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mAllAssessments.size();
    }

    public List<Instructor> GetInstructorByName(String name) {
        db.dbWriteExecutor.execute(() -> {
            mAllInstructors = mInstructorDAO.getInstructorByName(name);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return mAllInstructors;
    }

    public Term getTermByID(int id) {
        db.dbWriteExecutor.execute(() -> {
            term = mTermDAO.getTermByID(id);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return term;
    }

    public Course getCourseByID(int id) {
        db.dbWriteExecutor.execute(() -> {
            course = mCourseDAO.getCourseByID(id);
        });

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return course;
    }

}
