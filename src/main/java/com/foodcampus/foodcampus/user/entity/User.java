package com.foodcampus.foodcampus.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "test")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 10)
    private String nickname;

    @Column(length = 500)
    private String accessToken;     // 액세스 토큰

    @Column(length = 500)
    private String refreshToken;    // 리프레시 토큰

    @Column(length = 15)            // 전화번호 칼럼 추가
    private String phone;

    @Column
    private int role;           // 0: user, 1: admin

    @Column
    private String profileImage;



}
