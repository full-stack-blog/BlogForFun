package com.example.blog.Controllers;

import com.example.blog.Models.User;
import com.example.blog.Repositories.PostRepo;
import com.example.blog.Repositories.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private UserRepo userDoa;
    private PasswordEncoder passwordEncoder;
    private PostRepo postDao;

    public UserController(UserRepo userDoa, PasswordEncoder passwordEncoder, PostRepo postDao) {
        this.userDoa = userDoa;
        this.passwordEncoder = passwordEncoder;
        this.postDao = postDao;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userDoa.save(user);
        return "redirect:/login";
    }

    @GetMapping("/blogger-profile")
    public String gotToBlogger(Model model) {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userRoleBlogger", loggedIn);
        if(loggedIn.getUserRole().equals("blogger"))
            return "users/blogger-profile";
        else
            return "redirect:/login";
    }

    @GetMapping("/admin-profile")
    public String goToAdmin(Model model) {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userRoleAdmin", loggedIn);
        model.addAttribute("posts",postDao.findAll());
        if(loggedIn.getUserRole().equals("admin"))
            return "users/admin-profile";
        else
            return "redirect:/login";
    }


//    @GetMapping("/blogger-contact/{id}")
//    public String goToBreederContactInfo(@PathVariable long id, Model model){
//        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("blogger",usersdao.findUserByPost(postdao.findById(id)));
//        return "users/blogger-contact";
//    }
}
