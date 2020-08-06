package com.example.blog.Repositories;

import com.example.blog.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepo extends JpaRepository<Post, Long> {
    Post findByTitle(String title);
//    @Query(value = "select distinct p.* from posts as p left join categories as cat on p.id = cat.post_id left join responses as r on p.id = r.post_id left join comments as c on r.id = c.response_id where p.title like %?1% or p.description like %?2% or cat.name like %?3% or c.comment like %?4% order by p.id desc", nativeQuery = true)

}