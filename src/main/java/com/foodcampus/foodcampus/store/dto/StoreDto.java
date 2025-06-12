package com.foodcampus.foodcampus.store.dto;

import com.foodcampus.foodcampus.store.entity.Menu;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoreDto {
    private Long storeId;
    private String storeName;
    private String address;
    private String detailAddress;
    private String storeNumber;
    private String openTime;
    private String closeTime;
    private String selectMainCategory;
    private String storeInfo;
    private String shortInfo;
    private String storeImage;  //대표 사진 한 장

    private int taste;
    private int price;
    private int kindness;
    private int hygiene;
    private int score;      // 합산점수

    //위도경도
    private double latitude;
    private double longitude;

    private List<String> selectSubCategories;
    private List<String> closedDays;
    private List<String> url;   // 가게 사진 여러 장
    private List<Menu> menu;
}
