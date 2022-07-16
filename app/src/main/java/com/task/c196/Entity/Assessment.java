package com.task.c196.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private Date date;
    private String type;
    private Integer courseID;

    public Assessment(int id, String name, Date date, String type, Integer courseID) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.type = type;
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", courseID=" + courseID +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
    }
}
