package com.gemini4.loginpage.service;

import com.gemini4.loginpage.model.User;
import org.springframework.stereotype.Service;

public interface UserService {

    public User findUserByEmail(String email) ;
   // public User findByUsernamePassword(String username,String password);

    public User saveUser(User user);
}
