package com.foodcampus.foodcampus.review.repository;

import com.foodcampus.foodcampus.review.entity.Review;
import com.foodcampus.foodcampus.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
      List<Review> findByUserId(Long userId);
      List<Review> findByStore(Store store);
}
