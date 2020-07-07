package com.example.blog.Controllers;

import com.example.blog.Models.User;
import com.example.blog.Services.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {
    private EmailService emailService;

    public HomeController(EmailService emailService){
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String landingPage() {
        return "landing";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
        }
        return "users/home";
    }

    @GetMapping("/about-BlogForFun")
    public String aboutPage(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);

        }
        return "about-BlogForFun";
    }

    @GetMapping("/contact-us")
    public String contactPage(Model model){
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            SimpleMailMessage msg = new SimpleMailMessage();
            model.addAttribute("user", loggedIn);
            model.addAttribute(msg);
        }
        else{
            return "redirect:/login";
        }
        return "contact-us";
    }

    @PostMapping("/contact-us")
    public String contactForm(@RequestParam(name = "subject") String subject, @RequestParam(name = "text") String text){
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(u.getEmail());
        msg.setTo("blog4fun123@gmail.com");
        msg.setSubject(subject);
        msg.setText(text);
        emailService.prepareAndSend2(u.getEmail(), subject, text);
        return "redirect:/about-BlogForFun";
    }

}
