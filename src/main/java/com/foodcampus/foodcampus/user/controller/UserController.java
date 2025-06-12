package com.foodcampus.foodcampus.user.controller;

import com.foodcampus.foodcampus.user.dto.UserDto;
import com.foodcampus.foodcampus.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/auth")
public class UserController {

     private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

  @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        // UserService의 login 메서드에 UserDto 객체를 전달
        ResponseEntity<?> responseEntity = userService.login(userDto);
        return responseEntity;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        System.out.println(userDto);
        ResponseEntity<?> responseEntity = userService.signUp(userDto);
        System.out.println(responseEntity);
        return responseEntity;
    }

    // 사용자 정보 불러오기
    @GetMapping("/user")
    public UserDto getUserData(@RequestHeader("Authorization") String token) {
        return userService.getUserData(token);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Long userId) {
        ResponseEntity<String> responseEntity = userService.logout(userId);
        return responseEntity;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        return userService.refreshToken(refreshToken);
    }


    // 전화번호 변경
    @PostMapping("/update-phone")
    public ResponseEntity<String> updatePhoneNumber(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {
        if (request.containsKey("phone")) {
            String newPhone = request.get("phone");
            return userService.updatePhoneNumber(token, newPhone);
        } else {
            return ResponseEntity.badRequest().body("Phone number is missing.");
        }
    }

    // 닉네임 변경
    @PostMapping("/update-nickname")
    public ResponseEntity<String> updateNickname(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {
        if (request.containsKey("nickname")) {
            String newNickname = request.get("nickname");
            return userService.updateNickname(token, newNickname);
        } else {
            return ResponseEntity.badRequest().body("Nickname is missing.");
        }
    }

    // 탈퇴하기
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawUser(@RequestHeader("Authorization") String token) {
        ResponseEntity<String> responseEntity = userService.withdrawUser(token);
        return responseEntity;
    }

    //비밀번호 변경
    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {
        if (request.containsKey("currentPassword") && request.containsKey("newPassword")) {
            String currentPassword = request.get("currentPassword");
            String newPassword = request.get("newPassword");
            return userService.updatePassword(token, currentPassword, newPassword);
        } else {
            return ResponseEntity.badRequest().body("Current password or new password is missing.");
        }
    }
    // 프로필 이미지 URL 업데이트
    @PostMapping("/update-profile-image")
    public ResponseEntity<String> updateProfileImage(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {
        if (request.containsKey("imageUrl")) {
            String imageUrl = request.get("imageUrl");
            return userService.updateProfileImage(token, imageUrl);
        } else {
            return ResponseEntity.badRequest().body("Image URL is missing.");
        }
    }




}
