package com.example.blog.Controllers;

import com.example.blog.Models.Post;
import com.example.blog.Models.User;
import com.example.blog.Repositories.PostRepo;
import com.example.blog.Repositories.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public String welcome(Model model, @RequestParam(required = false) String search) {
        model.addAttribute("search", search);

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
                if (search != null) {
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
                    }
                }
            }
            model.addAttribute("posts", searchedPosts);

        }
        return "posts/index";
    }


//    @GetMapping("/user-posts/{id}")

    ////////search example from pantry chef!///////
//    @GetMapping("/recipes")
//    public String getPosts(Model model, @RequestParam(required = false) String search, @RequestParam(required = false, name = "categories") Long value) {
//        //=== SEARCH BAR ===//
//        model.addAttribute("search", search);
//        model.addAttribute("value", value);
//        model.addAttribute("sapi", sapi);
//
//        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
//            User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            model.addAttribute("user", u);
//        }
//        if (search == null && value == null) {
//            List<Recipe> recipes = recipeDao.findAll();
//            model.addAttribute("recipes", recipes);
//        } else {
//            List<Recipe> recipes = recipeDao.findAll();
//            List<Recipe> searchedRecipes = new ArrayList<>();
//
//            for (Recipe recipe : recipes) {
//                if (value != null) {
//                    boolean valueFlag = false;
//                    for (Categories category : recipe.getCategories()) {
//                        if (category.getId() == value) {
//                            System.out.println(value + " --> =? " + category.getId());
//
//                            searchedRecipes.add(recipe);
//                            valueFlag = true;
//                            break;
//                        }
//                    }
//                    if (valueFlag) {
//                        continue;
//                    }
//                }
//                if (search != null) {
//                    if (recipe.getTitle().toLowerCase().contains(search.toLowerCase())) {
//                        searchedRecipes.add(recipe);
//                        continue;
//                    }
//                    String[] searchArray = search.replaceAll(", ", ",").split(",");
//                    ArrayList<String> ingredientArray = new ArrayList<>();
//
//                    recipe.getIngredientList().forEach(ingredient -> {
//                        ingredientArray.add(ingredient.getName());
//                    });
//                    //separate ingredient string into an array
//                    boolean searchFlag = true;
//                    for (String s : searchArray) {
//                        if (!ingredientArray.toString().toLowerCase().contains(s.toLowerCase())) {
//                            searchFlag = false;
//                            break;
//                        }
//                    }
//                    if (searchFlag) {
//                        searchedRecipes.add(recipe);
//                    }
//                }
//            }
//            model.addAttribute("recipes", searchedRecipes);
//
//        }
//        return "recipes/recipes";
//    }

    @GetMapping("/user-posts/{id}")
    public String getIndividualPost(Model model, @PathVariable long id) {
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
    public String editPostFrom(Model model, @ModelAttribute Post post, @PathVariable long id, @RequestParam(name = "title") String title, @RequestParam(name = "body") String body, @RequestParam(name = "postImageUrl") String postImageUrl) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", loggedIn);
            model.addAttribute("posts", postDao.findAll());
        }
        Post editPost = postDao.getOne(id);
        editPost.setBody(body);
        editPost.setPostImageUrl(postImageUrl);
//        editPost.setUser(post.user);
        editPost.setTitle(title);
        postDao.save(editPost);
        return "redirect:/profile";
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable long id) {
        postDao.deleteById(id);
        return "redirect:/profile";
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


    @PostMapping("/admin-profile/{id}/delete")
    public String deletePostAdmin(@PathVariable long id){
        postDao.deleteById(id);
        return "redirect:/admin-profile";
    }

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
}
