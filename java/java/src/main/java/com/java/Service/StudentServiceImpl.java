package com.java.Service;

import java.util.List;

import com.java.Entity.Student;
import com.java.Repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepo studentRepo;

    @Override
    @Cacheable(value = "students")
    public List<Student> getStudentDetails() {
        System.out.println(">>> DB HIT: Fetching ALL students from database");
        return studentRepo.findAll();
    }

    @Override
    @CacheEvict(value = "students", allEntries = true)
    public void saveStudent(Student student) {
        System.out.println(">>> DB HIT: Saving student '" + student.getName() + "' & clearing cache");
        studentRepo.save(student);
    }

    @Override
    @Cacheable(value = "students", key = "#studentId")
    public Student getStudentById(Long studentId) {
        System.out.println(">>> DB HIT: Fetching student with ID " + studentId + " from database");
        return studentRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
    }

    @Override
    @CacheEvict(value = "students", key = "#studentId")
    public void deleteStudentById(Long studentId) {
        System.out.println(">>> DB HIT: Deleting student with ID " + studentId + " & clearing cache");
        studentRepo.deleteById(studentId);
    }

}
