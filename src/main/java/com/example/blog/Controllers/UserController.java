package com.example.blog.Controllers;

import com.example.blog.Models.User;
// import com.example.blog.Repositories.FavoritesRepo;
import com.example.blog.Repositories.PostRepo;
import com.example.blog.Repositories.UserRepo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private UserRepo userDoa;
    private PasswordEncoder passwordEncoder;
    private PostRepo postDao;
    // private FavoritesRepo favoritesDao;

    public UserController(UserRepo userDoa, PasswordEncoder passwordEncoder, PostRepo postDao) {
        this.userDoa = userDoa;
        this.passwordEncoder = passwordEncoder;
        this.postDao = postDao;
        // this.favoritesDao = favoritesDao;
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
            model.addAttribute("favorites", loggedIn.getFavorites());
            System.out.println(loggedIn.getFavorites().size());
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
// creates an optional pathway to use security authentication for each method instead of the whole site //
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
    public String editProfile(@ModelAttribute User user, @RequestParam(name = "profileImage") String profileImage) {
        User tempUser = new User();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            tempUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        user.setUserRole(tempUser.getUserRole());
        user.setPosts(tempUser.getPosts());
        user.setPassword(tempUser.getPassword());
        user.setId(tempUser.getId());
        user.setProfileImage(profileImage);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        userDoa.save(user);
        return "redirect:/profile-info-updated";
    }

    @RequestMapping("/profile/profile-edit-CCP")
//  Change current password alert //
    public String errorCCP(Model model){
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj == null || !(obj instanceof UserDetails)) {
            return "redirect:/login";
        }
        User tempUser = (User) obj;
        User user = userDoa.getOne(tempUser.getId());
        model.addAttribute("user", user);
        model.addAttribute("ccp",true);
        return "users/editProfile";
    }
    @RequestMapping("/profile/profile-edit-MP")
//  mismatch password alert //
    public String errorMP(Model model){
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj == null || !(obj instanceof UserDetails)) {
            return "redirect:/login";
        }
        User tempUser = (User) obj;
        User user = userDoa.getOne(tempUser.getId());
        model.addAttribute("user", user);
        model.addAttribute("mp",true);
        return "users/editProfile";
    }

    @PostMapping("/profile/password-edit")
    public String editPassword(@RequestParam String currentPass, @RequestParam String newPass, @RequestParam String confirmNewPass){
        User tempUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDoa.getOne(tempUser.getId());
//  this explains if the CCP equals what is in the DB, go create new PW //
        if (!BCrypt.checkpw(currentPass, user.getPassword())){
            return  "redirect:/profile/profile-edit-CCP";
//  this confirms that the new PW equals the confirmed PW //
        } else if (!newPass.equals(confirmNewPass)){
            return "redirect:/profile/profile-edit-MP";
        }
        String hash = passwordEncoder.encode(newPass);
        user.setPassword(hash);
        userDoa.save(user);
        return "redirect:/profile-password-updated";
    }

    @RequestMapping("/profile-info-updated")
    public String infoUpdated(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User tempUser = userDoa.getOne(user.getId());
        model.addAttribute("user", tempUser);
        model.addAttribute("sei", true);
        return "users/editProfile";
    }

    @RequestMapping("/profile-password-updated")
    public String passwordUpdated(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User tempUser = userDoa.getOne(user.getId());
        model.addAttribute("user", tempUser);
        model.addAttribute("scp", true);
        return "users/editProfile";
    }

//    @GetMapping("/buyer-profile")
//    public String gotToBuyer(Model model) {
//    User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    User user = userDoa.findUserById(loggedIn.getId());
//    model.addAttribute("userRoleBuyer", user);
//    model.addAttribute("favorites", user.getFavorites());
//    if(loggedIn.getUserRole().equals("buyer"))
//        return "users/buyer-profile";
//    else
//        return "redirect:/login";
//}

}
