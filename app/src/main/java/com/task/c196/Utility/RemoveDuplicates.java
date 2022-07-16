package com.task.c196.Utility;

import com.task.c196.Entity.Instructor;

import java.util.ArrayList;
import java.util.List;

public class RemoveDuplicates {
    private List<Instructor> instructorsWithoutDupes;
    private List<String> instructorNames;

    public List<Instructor> removeDupes(List<Instructor> instructors) {
        instructorsWithoutDupes = new ArrayList<>();
        instructorNames = new ArrayList<>();

        for (Instructor i : instructors) {
            if (!instructorNames.contains(i.getName())) {
                instructorNames.add(i.getName());
                instructorsWithoutDupes.add(i);
            }
        }

        return instructorsWithoutDupes;
    }
}
