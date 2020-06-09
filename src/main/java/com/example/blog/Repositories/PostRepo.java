package com.example.blog.Repositories;

import com.example.blog.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
    Post findByTitle(String title);
}