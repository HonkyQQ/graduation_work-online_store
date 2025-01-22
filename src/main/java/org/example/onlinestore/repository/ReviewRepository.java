package org.example.onlinestore.repository;

import org.example.onlinestore.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);


    List<Review> findByRating(int rating);


    @Query("SELECT r FROM Review r ORDER BY r.createdAt DESC")
    List<Review> findAllSortedByNewest();

    @Query("SELECT r FROM Review r ORDER BY r.createdAt ASC")
    List<Review> findAllSortedByOldest();
}