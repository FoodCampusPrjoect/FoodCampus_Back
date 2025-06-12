package com.foodcampus.foodcampus.review.controller;

import com.foodcampus.foodcampus.review.dto.ReviewDTO;
import com.foodcampus.foodcampus.review.entity.Review;
import com.foodcampus.foodcampus.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<String> createReview(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("받은 페이로드: " + payload);

            String reviewText = (String) payload.get("reviewText");
            if (reviewText == null || reviewText.isEmpty()) {
                throw new IllegalArgumentException("reviewText가 비어 있습니다.");
            }

            Integer totalScore = null;
            Object totalScoreObj = payload.get("totalScore");
            if (totalScoreObj instanceof Integer) {
                totalScore = (Integer) totalScoreObj;
            } else if (totalScoreObj instanceof Double) {
                totalScore = ((Double) totalScoreObj).intValue();
            } else {
                throw new IllegalArgumentException("totalScore의 타입이 올바르지 않습니다.");
            }

            Integer taste = (Integer) payload.get("taste");
            Integer price = (Integer) payload.get("price");
            Integer kindness = (Integer) payload.get("kindness");
            Integer hygiene = (Integer) payload.get("hygiene");

            List<String> imageUrls = (List<String>) payload.get("imageUrls");
            if (payload.get("storeId") == null) {
                throw new IllegalArgumentException("storeId가 비어 있습니다.");
            }
            Long storeId = ((Number) payload.get("storeId")).longValue();

            if (payload.get("userId") == null) {
                throw new IllegalArgumentException("userId가 비어 있습니다.");
            }
            Long userId = ((Number) payload.get("userId")).longValue();

            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setReviewText(reviewText);
            reviewDTO.setTotalScore(totalScore);
            reviewDTO.setTaste(taste);
            reviewDTO.setPrice(price);
            reviewDTO.setKindness(kindness);
            reviewDTO.setHygiene(hygiene);
            reviewDTO.setStoreId(storeId); // storeId 설정
            reviewDTO.setImageUrls(imageUrls); // 이미지 URL 목록 설정
            reviewDTO.setUserId(userId); // userId 설정

            System.out.println("저장할 리뷰: " + reviewDTO);
            reviewService.saveReview(reviewDTO);
            System.out.println("리뷰가 성공적으로 저장되었습니다.");
            return new ResponseEntity<>("리뷰가 성공적으로 생성되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("리뷰 생성 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("리뷰 생성 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<Review>> getReviewsByStoreId(@PathVariable Long storeId) {
        List<Review> reviews = reviewService.getReviewsByStoreId(storeId);
        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviews);
    }

    // 특정 사용자 ID로 리뷰를 가져오는 메서드
    @GetMapping("/reviews/user")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@RequestParam Long userId) {
        try {
            List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReviewById(reviewId);
            return new ResponseEntity<>("리뷰가 성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("리뷰 삭제 중 오류 발생: " + e.getMessage());
            return new ResponseEntity<>("리뷰 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
