package com.foodcampus.foodcampus.user.service;

import com.foodcampus.foodcampus.jwt.JwtTokenProvider;
import com.foodcampus.foodcampus.user.dto.UserDto;
import com.foodcampus.foodcampus.user.entity.*;
import com.foodcampus.foodcampus.user.entity.User;
import com.foodcampus.foodcampus.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
     private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider; // JwtTokenProvider 주입

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

   @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        return new CustomUserDetails(user);
    }

    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        Optional<User> userOptional = userRepository.findByEmail(userDto.getEmail());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("등록되지 않은 이메일입니다.");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return ResponseEntity.ok(tokens);
    }

    //회원가입
    public ResponseEntity<?> signUp(UserDto userDTO) {
        try {
            // 이메일 중복 확인
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 등록된 이메일입니다.");
            }

            // 닉네임 중복 확인
            if (userRepository.findByNickname(userDTO.getNickname()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용 중인 닉네임입니다.");
            }

            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setNickname(userDTO.getNickname());
            user.setPassword(encodedPassword);
            userRepository.save(user);

            // 가입 후 자동 로그인 처리
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

            // 사용자 엔티티에 토큰 저장
            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            // 토큰 반환
            Map<String, String> tokens = new HashMap<>();
            tokens.put("refreshToken", refreshToken);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            System.err.println("회원가입 과정에서 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 과정에서 오류가 발생했습니다");
        }
    }

    // 로그아웃
    public ResponseEntity<String> logout(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();

        // 사용자의 액세스 토큰과 리프레시 토큰을 삭제
        user.setAccessToken(null);
        user.setRefreshToken(null);
        userRepository.save(user); // 사용자 정보 업데이트

        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    // 사용자 정보 불러오기
    private UserDto mapUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setNickname(user.getNickname());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRole()); // 수정된 부분
        userDto.setProfileImage(user.getProfileImage());
        return userDto;
    }


    // 사용자 정보 불러오기
    public UserDto getUserData(String token) {
        String email = jwtTokenProvider.getEmail(token);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDto userDto = mapUserToUserDto(user);
            // 사용자의 롤 정보 추가
            userDto.setRole(user.getRole());

            return userDto;
        } else {
            throw new RuntimeException("User not found");
        }
    }


    // 리프레시 토큰으로 액세스 토큰 재발급
    public ResponseEntity<?> refreshToken(String refreshToken) {
        // 리프레시 토큰 검증
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "유효하지 않은 리프레시 토큰입니다."));
        }

        // 리프레시 토큰으로부터 사용자 정보 추출
        String userEmail = jwtTokenProvider.getEmail(refreshToken);

        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(userEmail);

        // 새 액세스 토큰 반환
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        // 리프레시 토큰은 그대로 유지하므로, 새로 발급하지 않음
        return ResponseEntity.ok(tokens);
    }

// 전화번호 변경
    public ResponseEntity<String> updatePhoneNumber(String token, String newPhone) {
        String email = jwtTokenProvider.getEmail(token);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPhone(newPhone);
            userRepository.save(user);
            return ResponseEntity.ok("전화번호가 성공적으로 업데이트되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

    // 닉네임 변경
    public ResponseEntity<String> updateNickname(String token, String newNickname) {
        String email = jwtTokenProvider.getEmail(token);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setNickname(newNickname);
            userRepository.save(user);
            return ResponseEntity.ok("닉네임이 성공적으로 업데이트되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

    // 탈퇴하기
    public ResponseEntity<String> withdrawUser(String token) {
        String email = jwtTokenProvider.getEmail(token);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.delete(user);
            return ResponseEntity.ok("사용자 정보가 성공적으로 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }
    //비밀번호 변경하기

    public ResponseEntity<String> updatePassword(String token, String currentPassword, String newPassword) {
        String email = jwtTokenProvider.getEmail(token);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("현재 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    // 프로필 이미지 URL 업데이트
    public ResponseEntity<String> updateProfileImage(String token, String imageUrl) {
        String email = jwtTokenProvider.getEmail(token);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setProfileImage(imageUrl);
            userRepository.save(user);
            return ResponseEntity.ok("프로필 이미지가 성공적으로 업데이트되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

}

