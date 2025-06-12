package com.foodcampus.foodcampus.review.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDTO {
    private String reviewText;
    private int totalScore;
    private int taste;
    private int price;
    private int kindness;
    private int hygiene;
    private Long storeId;
    private String storeName; // 가게 이름 추가
    private List<String> imageUrls; // 이미지 URL 목록 추가
    private Long userId;
    private Long reviewId; // 리뷰 ID
}
