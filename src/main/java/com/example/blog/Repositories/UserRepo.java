package com.example.blog.Repositories;

import com.example.blog.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUserRole(String users);
    User findUserById(long id);



}
