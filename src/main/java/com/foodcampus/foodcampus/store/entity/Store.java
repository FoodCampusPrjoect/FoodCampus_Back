package com.foodcampus.foodcampus.store.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(length = 30)
    private String storeName;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String detailAddress;

    @Column(length = 20)
    private String storeNumber;

    @Column(length = 20)
    private String openTime;

    @Column(length = 20)
    private String closeTime;

    @Column(length = 500)
    private String storeInfo;

    @Column(length = 50)
    private String shortInfo;

    @Column(length = 200)
    private String storeImage;

    @Column
    private int taste;

    @Column
    private int price;

    @Column
    private int kindness;

    @Column
    private int hygiene;

    @Column(length = 20)
    private int score;

    private double latitude;
    private double longitude;

    @Column(length = 50)
    private String selectMainCategory;

    @ElementCollection
    @CollectionTable(name = "categories", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "selectSubCategories", length = 500)
    private List<String> selectSubCategories;  // 여러 카테고리를 저장

    @ElementCollection
    @CollectionTable(name = "closed_days", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "closedDays", length = 500)
    private List<String> closedDays;  // 여러 휴무일을 저장

    @ElementCollection
    @CollectionTable(name = "store_images", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "url")
    private List<String> url;  // 여러 사진을 저장

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Menu> menu;

}
