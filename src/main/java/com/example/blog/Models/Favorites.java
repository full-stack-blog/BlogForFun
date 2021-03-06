// package com.example.blog.Models;

// import javax.persistence.*;
// import java.util.List;

// @Entity
// @Table(name="favorites")
// public class Favorites {

//     // Assigning the primary key and the automatic number generation
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private long id;

//     // Assigning the columns
//     @Column(nullable = false)
//     private String title;

//     @Column(nullable = false, length = 1000)
//     private String body;

//     @Column(nullable = false)
//     private String postImageUrl;

//     @Column(nullable = false)
//     private String access;

//     @ManyToOne
//     @JoinColumn(name = "user_id")
//     private User user;

//     @ManyToMany
//     @JoinTable(

//             name = "favorites_categories",
//             joinColumns = {@JoinColumn(name = "favorites_id")}
// //            inverseJoinColumns = {@JoinColumn(name = "post_id")}
//     )
//     private List<Categories> favCategories;

//     public Favorites() {}

//     public Favorites(Favorites copy) {
//         this.id = copy.id; // This line is SUPER important! Many things won't work if it's absent
//         this.title = copy.title;
//         this.body = copy.body;
//         this.user = copy.user;
//         this.access = copy.access;
//         this.favCategories = copy.favCategories;
//     }

//     public Favorites(long id, String title, String body, User user, String access, List<Categories> favCategories) {
//         this.id = id;
//         this.title = title;
//         this.body = body;
//         this.user = user;
//         this.access = access;
//         this.favCategories = favCategories;
//     }

//     public String getPostImageUrl() {
//         return postImageUrl;
//     }

//     public void setPostImageUrl(String postImageUrl) {
//         this.postImageUrl = postImageUrl;
//     }

//     public long getId() {
//         return this.id;
//     }

//     public void setId(long id) {
//         this.id = id;
//     }

//     public String getTitle() {
//         return this.title;
//     }

//     public void setTitle(String title) {
//         this.title = title;
//     }

//     public String getBody() {
//         return this.body;
//     }

//     public void setBody(String body) {
//         this.body = body;
//     }

//     public User getUser() {
//         return this.user;
//     }

//     public void setUser(User user) {
//         this.user = user;
//     }

//     public String getAccess() {
//         return access;
//     }

//     public void setAccess(String access) {
//         this.access = access;
//     }

//     public List<Categories> getFavCategories() {
//         return favCategories;
//     }

//     public void setFavCategories(List<Categories> favCategories) {
//         this.favCategories = favCategories;
//     }
// }

