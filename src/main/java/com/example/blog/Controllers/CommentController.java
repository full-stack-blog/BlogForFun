package com.example.blog.Controllers;

import com.example.blog.Models.Categories;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;

import com.example.blog.Models.Comment;
import com.example.blog.Models.Post;
import com.example.blog.Models.User;
import com.example.blog.Repositories.*;

// import com.example.blog.Repositories.PostRepo;

// import org.springframework.stereotype.Controller;

@Controller
public class CommentController {
    private CommentRepository commentDao;
    private PostRepo postDao;
    private UserRepo userDao;

    
    public CommentController(CommentRepository commentDao, PostRepo postDao, UserRepo userDao){
        this.commentDao = commentDao;
        this.postDao = postDao;
        this.userDao = userDao;
    }


    @GetMapping("/comments/post/{id}")
        public String getPostCommentForm(@PathVariable long id, Model model) {
             Post post = postDao.getOne(id);
            model.addAttribute("post", post);
            model.addAttribute("comment", new Comment());
            return "comments/comments";
        }

    //this will be the mapping for the comment creation form
    @PostMapping("/comments/post/{id}")
    public String createResponse(@PathVariable long id, Model model, @RequestParam String comment_txt) {
        Post post = postDao.getOne(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment com = new Comment();
        com.setComment_txt(comment_txt);
        com.setUser(user);
        com.setCreateDate(new Timestamp(System.currentTimeMillis()));
        com.setPost(post);

        commentDao.save(com);

        return "redirect:/post/" + id;
    }

    @PostMapping("comments/post/{id}/delete") 
        public String deleteComment(@PathVariable long id) {
            Post post = postDao.getOne(commentDao.getOne(id).getPost().getId());
            commentDao.deleteById(id);

            return "redirect:/post/" + post.getId();
        }

    @GetMapping("/comments/{id}/edit")
    public String EditPost(@PathVariable long id, Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
        }
        model.addAttribute("comment", commentDao.getOne(id));
        return "comments/editComment";
    }

    @PostMapping("/comments/{id}/edit")
    public String editPostFrom(Model model, @ModelAttribute Comment comment, @PathVariable long id, @RequestParam(name = "comment_txt") String comment_txt) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
            model.addAttribute("comment", commentDao.findAll());
        }
        Comment commentEdit = commentDao.getOne(id);
        commentEdit.setComment_txt(comment_txt);
//        commentEdit.setCreateDate(new Timestamp(System.currentTimeMillis())); not updating
        commentDao.save(commentEdit);
        return "redirect:/post/" + commentEdit.getPost().getId();
    }

}



