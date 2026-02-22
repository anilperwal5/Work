package com.java.Service;

import java.util.List;

import com.java.Entity.Student;


public interface StudentService {

    List<Student> getStudentDetails();
     void saveStudent(Student student);
     Student getStudentById(Long studentId);
     void deleteStudentById(Long studentId);


}
