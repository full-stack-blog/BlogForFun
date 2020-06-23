package com.example.blog.Controllers;

import com.example.blog.Models.Post;
import com.example.blog.Models.User;
import com.example.blog.Repositories.PostRepo;
import com.example.blog.Repositories.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

    private UserRepo userDoa;
    private PostRepo postDao;

    public PostController(UserRepo userDoa, PostRepo postDao) {
        this.userDoa = userDoa;
        this.postDao = postDao;
    }

    @GetMapping("/posts")
    public String welcome(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("blogger", loggedIn);
        }
        List<Post> posts = postDao.findAll();
        System.out.println(posts);
        model.addAttribute("posts", posts);
        return "posts/index";
    }

    @GetMapping("/blogger-posts/{id}")
    public String getIndividualPost(Model model, @PathVariable long id) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("blogger", loggedIn);
        }
        Post post = postDao.getOne(id);
        model.addAttribute("post", post);
        return "individualPost";
    }

    @GetMapping("/posts/create")
    public String createPostForm(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("blogger", loggedIn);
        }
            model.addAttribute("post", new Post());
            return "posts/create";

    }

    @PostMapping("/posts/create")
    public String SubmitPost(@RequestParam(name = "title") String title, @RequestParam(name = "body") String body, @RequestParam(name = "postImageUrl") String postImageUrl) {
        Post post = new Post();
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setTitle(title);
        post.setBody(body);
        post.setPostImageUrl("https://picsum.photos/seed/picsum/200/300");
        post.setUser(u);
        postDao.save(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/edit")
    public String EditPost(@PathVariable long id, Model model) {
        model.addAttribute("posts", postDao.getOne(id));
        return "posts/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String editPostFrom(@PathVariable long id, @RequestParam(name = "title") String title, @RequestParam(name = "body") String body, @RequestParam(name = "postImageUrl") String postImageUrl) {
        Post editPost = postDao.getOne(id);
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        editPost.setBody(body);
        editPost.setPostImageUrl(postImageUrl);
        editPost.setUser(u);
        editPost.setTitle(title);
        postDao.save(editPost);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable long id) {
        postDao.deleteById(id);
        return "/posts/index";
    }

    @PostMapping("/post/{id}")
    public String viewIndividualPost(@PathVariable long id, Model model) {
        Post post = postDao.getOne(id);
        model.addAttribute("post", post);
        return "individualPost";
    }

//    @PostMapping("/admin-profile/{id}/delete")
//    public String deletePostAdmin(@PathVariable long id){
//        postDao.deleteById(id);
//        return "redirect:/admin-profile";
//    }
//
//    @GetMapping("/admin-profile/{id}/update2")
//    public String updateDogPostFormAdmin(@PathVariable long id, Model model) {
//        model.addAttribute("allPosts", postDao.getOne(id));
//        return "breeder-posts/update2";
//    }
//
//    @PostMapping("/admin-profile/{id}/update2")
//    public String updateBreederPostAdmin(@PathVariable long id, @RequestParam String dogBreed, @RequestParam String dogGroup, @RequestParam String dogDescription, @RequestParam String dogPrice, @RequestParam String images) {
//        postDao Post = PostDao.getOne(id);
//        Post.set;
//        Post.set;
//        Post.set;
//        Post.set;
//        Post.set;
//        postDao.save(Post);
//        return "redirect:/admin-profile";
//    }
}
