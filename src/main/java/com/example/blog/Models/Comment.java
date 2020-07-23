package com.example.blog.Models;

import javax.persistence.*;


@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long comment_id;

    @Column(name = "comment")
    private String comment_txt;

    

    
}