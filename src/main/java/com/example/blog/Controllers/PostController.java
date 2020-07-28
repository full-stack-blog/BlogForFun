package com.example.blog.Controllers;

import com.example.blog.Models.Categories;
import com.example.blog.Models.Favorites;
import com.example.blog.Models.Post;
import com.example.blog.Models.User;
import com.example.blog.Repositories.FavoritesRepo;
import com.example.blog.Repositories.PostRepo;
import com.example.blog.Repositories.UserRepo;
import com.example.blog.Services.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    private EmailService emailService;
    private UserRepo userDoa;
    private PostRepo postDao;
    private EmailService emailservice;
    private FavoritesRepo favoritesDao;

    public PostController(UserRepo userDoa, PostRepo postDao, EmailService emailservice, FavoritesRepo favoritesDao) {
        this.userDoa = userDoa;
        this.postDao = postDao;
        this.emailservice = emailservice;
        this.favoritesDao = favoritesDao;
    }

    @GetMapping("/posts")
    public String welcome(Model model, @RequestParam(required = false) String search) {
        model.addAttribute("search", search);
// This search code allows for searches that are not case sensitive, and can be searched by categories  //
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
        }
        if (search == null) {
            List<Post> posts = postDao.findAll();
            model.addAttribute("posts", posts);
        }else {
            List<Post> posts = postDao.findAll();
            List<Post> searchedPosts = new ArrayList<>();
            for (Post post : posts) {
                if (search.length() > 0) {
//                    System.out.println(search.length());
                    if (post.getTitle().toLowerCase().contains(search.toLowerCase())) {
                        searchedPosts.add(post);
                        continue;
                    }
                    if (post.getBody().toLowerCase().contains(search.toLowerCase())) {
                        searchedPosts.add(post);
                        continue;
                    }
                    if(post.getUser().getUsername().toLowerCase().contains(search.toLowerCase())){
                        searchedPosts.add(post);
                        continue;
                    }
                    if(post.getCategories().toArray().length > 0){
//                        System.out.println("inside categories array length  :  " + post.getCategories().toArray().length);
                        for(Categories category : post.getCategories()){
//                            System.out.println(category.getName() + " : " + post.getId());
                            if(category.getName().toLowerCase().contains(search.toLowerCase())){
                                searchedPosts.add(post);
//                                System.out.println("added " + post.getId());
                            }
                        }
                    }

                }
            }
            model.addAttribute("posts", searchedPosts);

        }
        return "posts/index";
    }

    @GetMapping("/contact-us/{id}")
    public String contactPage(Model model, @PathVariable long id){

        Post post = postDao.getOne(id);
        model.addAttribute("post", post);
        EmailService emailService = new EmailService();
        model.addAttribute("emailService", emailService);
        String subject = emailService.getSubject();
        String body = emailService.getBody();
        model.addAttribute("subject", subject);
        model.addAttribute("body", body);

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
        }
        else{
            return "redirect:/login";
        }
        return "contact-us";
    }
// contact admin and report posts method //
    @PostMapping("/contact-us/{id}")
    public String contactForm(@RequestParam(name = "subject") String subject, @RequestParam(name = "body") String body, @PathVariable long id){
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String desc = "Complainant: " + u.getEmail() + "\nComplaint: " + body;
        EmailService emailService = new EmailService();
        emailService.setBody(desc);
        emailService.setSubject(subject);
        emailService.setFrom("blog4fun123@gmail.com");
        Post post = postDao.getOne(id);
        emailservice.prepareAndSend2(subject, desc);
        return "redirect:/post/" + post.getId();
    }

    @GetMapping("/post/{id}")
    public String viewIndividualPost(@PathVariable long id, Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
        }
        Post post = postDao.getOne(id);
        model.addAttribute("post", post);
        return "posts/individualPost";
    }

    @GetMapping("/posts/create")
    public String createPostForm(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
        }
            model.addAttribute("post", new Post());
            return "posts/create";
    }

    @PostMapping("/posts/create")
    public String SubmitPost(@RequestParam(name = "title") String title, @RequestParam(name = "body") String body, @RequestParam(name = "postImageUrl") String postImageUrl, @RequestParam(name = "access") String access, @RequestParam List<Categories> categories) {
        Post post = new Post();
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setTitle(title);
        post.setBody(body);
        post.setPostImageUrl(postImageUrl);
//        if(postImageUrl != null){
//            post.setPostImageUrl(postImageUrl);
//        }
//        else{
//            post.setPostImageUrl('example here');
//        }
        post.setUser(u);
        post.setAccess(access);
        post.setCategories(categories);
        postDao.save(post);
//        emailservice.prepareAndSend(post, "you created a Post", "Title:" + post.getTitle() + "\nDescription: " + post.getBody());
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/edit")
    public String EditPost(@PathVariable long id, Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
        }
        model.addAttribute("post", postDao.getOne(id));
        return "posts/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String editPostFrom(Model model, @RequestParam List<Categories> categories, @ModelAttribute Post post, @PathVariable long id, @RequestParam(name = "title") String title, @RequestParam(name = "body") String body, @RequestParam(name = "postImageUrl") String postImageUrl) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
            model.addAttribute("posts", postDao.findAll());
        }
        Post editPost = postDao.getOne(id);
        editPost.setBody(body);
        editPost.setPostImageUrl(postImageUrl);
        editPost.setUser(editPost.getUser());
        editPost.setTitle(title);
        editPost.setId(id);
        editPost.setCategories(categories);
        postDao.save(editPost);
        return "redirect:/profile";
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable long id) {
        postDao.deleteById(id);
        return "redirect:/profile";
    }

    @PostMapping("/favorites/{id}/delete")
    public String deleteFavorite(@PathVariable long id) {
        favoritesDao.deleteById(id);
        return "redirect:/profile";
    }



//    @PostMapping("/admin-profile/{id}/delete")
//    public String deletePostAdmin(@PathVariable long id){
//        postDao.deleteById(id);
//        return "redirect:/admin-profile";
//    }

//    @GetMapping("/admin-profile/{id}/update2")
//    public String updateDogPostFormAdmin(@PathVariable long id, Model model) {
//        model.addAttribute("allPosts", postDao.getOne(id));
//        return "posts/update2";
//    }

//    @PostMapping("/admin-profile/{id}/update2")
//    public String updateBreederPostAdmin(@PathVariable long id, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String dogDescription, @RequestParam String dogPrice, @RequestParam String images) {
//        postDao Post = PostDao.getOne(id);
//        Post.set;
//        Post.set;
//        Post.set;
//        Post.set;
//        Post.set;
//        postDao.save(Post);
//        return "redirect:/admin-profile";
//    }

    @PostMapping("/favorite/{id}")
    public String favorite(@PathVariable long id, Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
            Post post = postDao.getOne(id);
            Favorites favorites = new Favorites();
            favorites.setAccess(post.getAccess());
            favorites.setBody(post.getBody());
            favorites.setFavCategories(post.getCategories());
            favorites.setPostImageUrl(post.getPostImageUrl());
            favorites.setUser(loggedIn);
            favorites.setTitle(post.getTitle());
            favorites.setId(post.getId());
            favoritesDao.save(favorites);
            return "redirect:/profile";

        }
        return "redirect:/login";
    }

}
