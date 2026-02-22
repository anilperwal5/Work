package com.java.Controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.Config.JwtHelper;
import com.java.Entity.User;
import com.java.Form.UserForm;
import com.java.Service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper jwtHelper;


    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/signup")
    public String SignUp() {
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String email, @RequestParam String password,
                                HttpServletResponse response, Model model) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String token = jwtHelper.generateToken(userDetails);

            // Store JWT in an HttpOnly cookie
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(5 * 60 * 60); // 5 hours
            response.addCookie(cookie);

            return "redirect:/user/userPage";
        } catch (BadCredentialsException e) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Clear the JWT cookie
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login?logout";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userForm") UserForm userForm, BindingResult result, Model model) {
        // create a new User entity from the submitted form
        User user = new User(UUID.randomUUID().toString(), userForm.getName(), userForm.getEmail(), passwordEncoder.encode(userForm.getPassword()));
        System.out.println(user);
        userService.saveUser(user); // Save the user to the database using the service layer
       System.out.println("user saved successfully");
        model.addAttribute("user", user);
        // return a view name after registration; adjust as needed
        return "signup";
    }

}
