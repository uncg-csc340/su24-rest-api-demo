package com.csc340.restapidemo;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private List<Student> students;

    public StudentRepository() {
        students = new ArrayList<Student>();
        populateDefaultStudents();
    }

    private void populateDefaultStudents() {
        students.add(new Student(1, "sample1", "csc", 3.12));
        students.add(new Student(2, "sample2", "eng", 2.73));
        students.add(new Student(3, "sample3", "mat", 3.69));
    }

    public List<Student> getAllStudents() {
        return students;
    }


    public Student getStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    public void deleteStudentById(int id) {
        students.removeIf(student -> student.getId() == id);
    }

    public void createStudent(Student student) {
        students.add(student);
    }

    public void updateStudent(int id, Student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            if (student.getId() == id) {
                updatedStudent.setId(id);
                students.set(i, updatedStudent);
                return;
            }
        }

    }
}
