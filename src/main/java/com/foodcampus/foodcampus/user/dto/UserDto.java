package com.foodcampus.foodcampus.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String email;           //이름
    private String password;        //비밀번호
    private String nickname;        //닉네임
    private String accessToken;     //액세스 토큰
    private String refreshToken;    //리프레시 토큰
    private Long id;                //유니크 id
    private String phone;           // 전화번호 추가
    private int role;
    private String profileImage;

}
