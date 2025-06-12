package com.foodcampus.foodcampus.user.entity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

      private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 모든 사용자에게 기본적으로 "ROLE_USER" 권한을 부여
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // 계정이 만료되지 않았는지 확인
    @Override
    public boolean isAccountNonExpired() {
        return true; // 예시에서는 항상 true를 반환
    }

    // 계정이 잠겨있지 않은지 확인
    @Override
    public boolean isAccountNonLocked() {
        return true; // 예시에서는 항상 true를 반환
    }

    // 자격 증명이 만료되지 않았는지 확인
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 예시에서는 항상 true를 반환
    }

    // 계정이 활성화되어 있는지 확인
    @Override
    public boolean isEnabled() {
        return true; // 예시에서는 항상 true를 반환
    }
}
