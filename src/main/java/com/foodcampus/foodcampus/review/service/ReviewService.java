package com.foodcampus.foodcampus.review.service;

import com.foodcampus.foodcampus.review.dto.ReviewDTO;
import com.foodcampus.foodcampus.review.entity.Review;
import com.foodcampus.foodcampus.review.repository.ReviewRepository;
import com.foodcampus.foodcampus.store.entity.Store;
import com.foodcampus.foodcampus.store.repository.StoreRepository;
import com.foodcampus.foodcampus.user.entity.User;
import com.foodcampus.foodcampus.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private StoreRepository storeRepository; // StoreRepository 추가

    @Autowired
    private UserRepository userRepository; // UserRepository 추가

    public Review saveReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setReviewText(reviewDTO.getReviewText());
        review.setTotalScore(reviewDTO.getTotalScore());
        review.setTaste(reviewDTO.getTaste());
        review.setPrice(reviewDTO.getPrice());
        review.setKindness(reviewDTO.getKindness());
        review.setHygiene(reviewDTO.getHygiene());

        // Store 엔티티 가져오기
        Optional<Store> storeOptional = storeRepository.findById(reviewDTO.getStoreId());
        if (storeOptional.isPresent()) {
            review.setStore(storeOptional.get()); // Store 설정
        } else {
            throw new IllegalArgumentException("해당 storeId에 해당하는 가게가 없습니다.");
        }

        // User 엔티티 가져오기
        Optional<User> userOptional = userRepository.findById(reviewDTO.getUserId());
        if (userOptional.isPresent()) {
            review.setUser(userOptional.get()); // User 설정
        } else {
            throw new IllegalArgumentException("해당 userId에 해당하는 유저가 없습니다.");
        }

        review.setImageUrls(reviewDTO.getImageUrls()); // 이미지 URL 목록 설정
        return reviewRepository.save(review);
    }

    @Transactional
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Transactional
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    // 특정 store_id에 해당하는 모든 리뷰를 조회하는 메소드 추가
    @Transactional
    public List<Review> getReviewsByStoreId(Long storeId) {
        // Store 엔티티를 가져오는 로직
        Optional<Store> store = storeRepository.findById(storeId);
        if (!store.isPresent()) {
            throw new IllegalArgumentException("해당 storeId에 해당하는 가게가 없습니다.");
        }
        // Store에 해당하는 리뷰 조회
        return reviewRepository.findByStore(store.get());
    }

    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReviewId(review.getId()); // 리뷰 아이디 설정
        reviewDTO.setReviewText(review.getReviewText());
        reviewDTO.setTotalScore(review.getTotalScore());
        reviewDTO.setTaste(review.getTaste());
        reviewDTO.setPrice(review.getPrice());
        reviewDTO.setKindness(review.getKindness());
        reviewDTO.setHygiene(review.getHygiene());
        reviewDTO.setStoreId(review.getStore().getStoreId());
        reviewDTO.setUserId(review.getUser().getId());
        reviewDTO.setStoreName(review.getStore().getStoreName()); // 가게 이름 설정
        reviewDTO.setImageUrls(review.getImageUrls());
        return reviewDTO;
    }

    @Transactional
    public void deleteReviewById(Long reviewId) {
        if (reviewId == null) {
            throw new IllegalArgumentException("Review ID must not be null.");
        }
        reviewRepository.deleteById(reviewId);
    }

}
