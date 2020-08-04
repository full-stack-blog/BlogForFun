package com.example.blog.Models;

import java.sql.Timestamp;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.ocpsoft.prettytime.PrettyTime;


@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long comment_id;

    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment_txt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToMany
    @JoinColumn(name = "user_id")
    private User user;


    @Column (nullable = false)
    String commentedAt;

    @Column(name = "create_date", updatable = false)
    @CreationTimestamp
    private Timestamp createDate;

    private Post post_id;

    private User user_id;

   

    /////////// Constructors ////////////////

    public Comment() {

    }

    public Comment(long comment_id, String comment_txt, Post post_id){
        this.comment_id = comment_id;
        this.comment_txt = comment_txt;
        this.post_id = post_id;
    }

    public Comment(long comment_id, String comment_txt, Post post_id, User user_id){
        this.comment_id = comment_id;
        this.comment_txt = comment_txt;
        this.post_id = post_id;
        this.user_id = user_id;

    }

    //***** GETTERS AND SETTERS *****//

    public long getComment_id() {
        return this.comment_id;
    }

    public void setComment_id(long comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_txt() {
        return this.comment_txt;
    }

    public void setComment_txt(String comment_txt) {
        this.comment_txt = comment_txt;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentedAt() {
        return this.commentedAt;
    }

    public void setCommentedAt(String commentedAt) {
        this.commentedAt = commentedAt;
    }

    public Post getPost_id() {
        return this.post_id;
    }

    public void setPost_id(Post post_id) {
        this.post_id = post_id;
    }

    public User getUser_id() {
        return this.user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

	public void setCreateDate(Timestamp timestamp) {
    }
    
    public String prettyDate(){
        PrettyTime p = new PrettyTime();
        return p.format(this.createDate);
    }

    public String prettyUpdate(){
        PrettyTime p = new PrettyTime();
        return p.format(this.createDate);
    }
    
    // public long getCommentId() {
    //     return this.comment_id;
    // }

    // public void setCommentId(long comment_id) {
    //     this.comment_id = comment_id;
    // }

    // public PostRepo getCommentTxt() {
    //     return this.comment_txt;
    // }

    // ///  ?????????? Can I set a String for this     ///
    // // public void setCommentTxt(PostRepo post) {
    // //     this.comment_txt = post;
    // // }

    // public Post getPostId() {
    //     return this.post_id;
    // }

    // public void setPostId(Post post_id) {
    //     this.post_id = post_id;
    // }
  
    //  public User getUserId() {
    //     return this.user_id;
    // }

    // public void setUserId(User user_id) {
    //     this.user_id = user_id;
    // }



}