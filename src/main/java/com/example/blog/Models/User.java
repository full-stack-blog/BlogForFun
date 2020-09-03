package com.example.blog.Models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, name = "user_role")
    private String userRole;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false)
    private String coverImg;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @ManyToMany(mappedBy = "favorites", fetch = FetchType.EAGER) //, cascade = CascadeType.ALL
    private List<Post> favorites = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Comment> comments;

    public User() {
    }



    public User(User copy) {
        id = copy.id; // This line is SUPER important! Many things won't work if it's absent
        userRole = copy.userRole;
        email = copy.email;
        username = copy.username;
        password = copy.password;
        posts = copy.posts;
        lastName = copy.lastName;
        firstName = copy.firstName;
        profileImage = copy.profileImage;
        favorites = copy.favorites;
        comments = copy.comments;
        coverImg = copy.coverImg;
    }

    public User(long id, String userRole, String username, String firstName, String lastName, String password, String email, String profileImage, String coverImg, List<Post> posts, List<Post> favorites, List<Comment> comments) {
        this.id = id;
        this.userRole = userRole;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
        this.coverImg = coverImg;
        this.posts = posts;
        this.favorites = favorites;
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public List<Post> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Post> favorites) {
        this.favorites = favorites;
    }

    public void addFavorite(Post post) {
        this.posts.add(post);
        post.getFavorites().add(this);
    }
    public void removeFavorite(Post post) {
        this.posts.remove(post);
        post.getFavorites().remove(this);
    }
    public Boolean containsPost(Post post) {
        return this.favorites.contains(post);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }
}
