package com.java.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.Entity.Student;
import com.java.Repository.StudentRepo;
import com.java.Service.StudentService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/userPage")
    public String UserPage(){
        return "user";
    }

    @GetMapping("/student")
    public String Student(Model model) {
        return "student";
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@RequestParam String name, @RequestParam String email,
                              @RequestParam String address, @RequestParam String school, Model model) {
        try {
            Student student = new Student(name, email, address, school);
            studentService.saveStudent(student);
            model.addAttribute(
                "success", "Student saved successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to save student: " + e.getMessage());
        }
        return "student";
    }

    @GetMapping("/showStudent")
    public String showStudent(Model model) {
        System.out.println("========================================");
        System.out.println(">>> Calling getStudentDetails()...");

        long startTime = System.currentTimeMillis();
        List<Student> studentDetails = studentService.getStudentDetails();
        long timeTaken = System.currentTimeMillis() - startTime;

        if (timeTaken < 5) {
            System.out.println(">>> DATA FROM CACHE | Time: " + timeTaken + "ms");
        } else {
            System.out.println(">>> DATA FROM DATABASE | Time: " + timeTaken + "ms");
        }
        System.out.println("========================================");

        model.addAttribute("students", studentDetails);
        return "studentpage";
    }



}
