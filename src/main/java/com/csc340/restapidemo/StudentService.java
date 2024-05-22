package com.csc340.restapidemo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService() {
        this.studentRepository = new StudentRepository();
    }

    public List<Student> getAllStudents() {

        return studentRepository.getAllStudents();
    }

    public Student getStudentById(int id) {

        return studentRepository.getStudentById(id);
    }

    public void deleteStudentById(int id) {
        studentRepository.deleteStudentById(id);
    }

    public void createStudent(Student student) {
        studentRepository.createStudent(student);
    }

    public void updateStudent(int id, Student updatedStudent) {
        studentRepository.updateStudent(id, updatedStudent);
    }
}
