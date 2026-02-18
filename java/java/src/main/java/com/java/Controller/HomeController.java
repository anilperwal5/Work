package com.java.Controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.java.Entity.User;
import com.java.Form.UserForm;
import com.java.Service.UserService;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/signup")
    public String SignUp() {
        return "signup";
    }


    @PostMapping("/register")
    public String register(@ModelAttribute("userForm") UserForm userForm, BindingResult result, Model model) {
        // create a new User entity from the submitted form
        User user = new User(UUID.randomUUID().toString(), userForm.getName(), userForm.getEmail(), userForm.getPassword());
        System.out.println(user);
        userService.saveUser(user); // Save the user to the database using the service layer
       System.out.println("user saved successfully");
        model.addAttribute("user", user);
        // return a view name after registration; adjust as needed
        return "signup";
    }

}
