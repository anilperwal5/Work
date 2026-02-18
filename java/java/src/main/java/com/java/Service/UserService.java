package com.java.Service;

import com.java.Entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void saveUser(User user);

}
