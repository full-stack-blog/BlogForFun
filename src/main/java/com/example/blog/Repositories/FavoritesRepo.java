package com.example.blog.Repositories;

import com.example.blog.Models.Categories;
import com.example.blog.Models.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesRepo extends JpaRepository<Favorites, Long> {

}
