package com.gemini4.loginpage.repository;

import com.gemini4.loginpage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByEmail(String email);
   // @Query("from User where email=?1")
    //public List<User> findByEMAIL(String email);

   // @Query("from User where email=?1 and password=?2")
   // public User findByUsernamePassword(String username,String password);
}


