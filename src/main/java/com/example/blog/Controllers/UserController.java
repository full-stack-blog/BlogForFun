package com.example.blog.Controllers;

import com.example.blog.Models.User;
import com.example.blog.Repositories.PostRepo;
import com.example.blog.Repositories.UserRepo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        user.setUserRole("blogger");
        user.setProfileImage("https://picsum.photos/seed/picsum/200/300");
        userDoa.save(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String gotToBlogger(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
            model.addAttribute("posts", postDao.findAll());
        }
        return "users/profile";
    }

    @GetMapping("/admin-profile")
    public String goToAdmin(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
            model.addAttribute("posts", postDao.findAll());
        }
            return "users/admin-profile";
    }


    @GetMapping("/profile/edit")
    public String getEditProfileForm(Model model) {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj == null || !(obj instanceof UserDetails)) {
            return "redirect:/login";
        }
        User tempUser = (User) obj;
        User user = userDoa.getOne(tempUser.getId());
        model.addAttribute("user", user);
        return "users/editProfile";
    }
    @PostMapping("/profile/edit")
    public String editProfile(@ModelAttribute User user) {
        User tempUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setUserRole(tempUser.getUserRole());
        user.setPosts(tempUser.getPosts());
        user.setPassword(tempUser.getPassword());
        user.setId(tempUser.getId());
        user.setProfileImage("https://picsum.photos/seed/picsum/200/300");
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        userDoa.save(user);
        return "redirect:/profile";
    }

//    @RequestMapping("/profile/profile-edit-CCP")
//    public String errorCCP(Model model){
//        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (obj == null || !(obj instanceof UserDetails)) {
//            return "redirect:/login";
//        }
//        User tempUser = (User) obj;
//        User user = userRepo.getOne(tempUser.getId());
//        model.addAttribute("user", user);
//        model.addAttribute("ccp",true);
//        return "user/profile-edit";
//    }
//    @RequestMapping("/profile/profile-edit-MP")
//    public String errorMP(Model model){
//        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (obj == null || !(obj instanceof UserDetails)) {
//            return "redirect:/login";
//        }
//        User tempUser = (User) obj;
//        User user = userRepo.getOne(tempUser.getId());
//        model.addAttribute("user", user);
//        model.addAttribute("mp",true);
//        return "user/profile-edit";
//    }

//    @GetMapping("/blogger-contact/{id}")
//    public String goToBreederContactInfo(@PathVariable long id, Model model){
//        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("blogger",usersdao.findUserByPost(postdao.findById(id)));
//        return "users/blogger-contact";
//    }
}
