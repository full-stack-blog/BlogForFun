package com.example.blog.Controllers;

import com.example.blog.Models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String landingPage(Model model) {
        return "landing";
    }
    @GetMapping("/index")
    public String welcome(Model model) {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("showUserRoles", loggedIn);
        return "index";
    }
    @GetMapping("/home")
    public String homePage(Model model){
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("showUserRoles", loggedIn);
        return "blogger-posts/home";
    }
    @GetMapping("/about-BlogForFun")
    public String aboutPage(Model model){
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("showUserRoles", loggedIn);
        return "about-BlogForFun";
    }
    @GetMapping("/about")
    public String visitorAboutPage(){
        return "about";
    }
}
