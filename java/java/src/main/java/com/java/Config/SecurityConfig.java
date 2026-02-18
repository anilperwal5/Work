package com.java.Config;

import java.security.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
      
        http.authorizeHttpRequests(Authorize->{
            Authorize.requestMatchers("/home", "/signup", "/register", "/login").permitAll()
            .anyRequest().authenticated();
        }).formLogin(form->{
            form.loginPage("/login");
            form.loginProcessingUrl("/authenticate");
            form.defaultSuccessUrl("/user/userPage");
            form.failureUrl("/login?error=true");
            form.usernameParameter("email");
            form.passwordParameter("password");
        }).csrf().disable()
        .logout(logout->{
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/login?logout=true");

        });
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }


