package com.example.blog.Controllers;

import com.example.blog.Models.Categories;
// import com.example.blog.Models.Favorites;
import com.example.blog.Models.Post;
import com.example.blog.Models.User;
// import com.example.blog.Repositories.FavoritesRepo;
import com.example.blog.Repositories.PostRepo;
import com.example.blog.Repositories.UserRepo;
import com.example.blog.Services.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    // private FavoritesRepo favoritesDao;

    public PostController(UserRepo userDoa, PostRepo postDao, EmailService emailservice) {
        this.userDoa = userDoa;
        this.postDao = postDao;
        this.emailservice = emailservice;
        // this.favoritesDao = favoritesDao;
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
        } else {
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
                    if (post.getUser().getUsername().toLowerCase().contains(search.toLowerCase())) {
                        searchedPosts.add(post);
                        continue;
                    }
                    if (post.getCategories().toArray().length > 0) {
//                        System.out.println("inside categories array length  :  " + post.getCategories().toArray().length);
                        for (Categories category : post.getCategories()) {
//                            System.out.println(category.getName() + " : " + post.getId());
                            if (category.getName().toLowerCase().contains(search.toLowerCase())) {
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
    public String contactPage(Model model, @PathVariable long id) {

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
        } else {
            return "redirect:/login";
        }
        return "contact-us";
    }

    // contact admin and report posts method //
    @PostMapping("/contact-us/{id}")
    public String contactForm(@RequestParam(name = "subject") String subject, @RequestParam(name = "body") String body, @PathVariable long id) {
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
            User user = userDoa.getOne(loggedIn.getId());
            model.addAttribute("user", user);
        }
        Post post = postDao.getOne(id);
        model.addAttribute("post", post);
        model.addAttribute("posts", postDao.findAll());
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
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postDao.getOne(id);
        postDao.delete(post);
        System.out.println("before update user2");
        return "redirect:/profile";
    }

    @PostMapping("/favorite/{id}")
    public String addToFavorites(@PathVariable long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDoa.findUserById(loggedInUser.getId());
        Post post = postDao.getOne(id);
        user.addFavorite(post);
        userDoa.save(user);
        postDao.save(post);
        return "redirect:/profile";
    }

    @PostMapping("/favorites/{id}/delete")
    public String removeFromFavorites(@ModelAttribute User user, @PathVariable long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userDoa.findUserById(loggedInUser.getId());
        Post post = postDao.getOne(id);
        user.removeFavorite(post);
        userDoa.save(user);
        postDao.save(post);
        return "redirect:/profile";
    }
}
