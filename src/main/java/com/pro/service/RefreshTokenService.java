package com.pro.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RefreshTokenService {

    // 유저 아이디를 key로, 리프레시 토큰을 저장
    private final Map<String, String> refreshTokenStore = new ConcurrentHashMap<>();

    // 유저의 RefreshToken 저장
    public void save(String userId, String refreshToken) {
        refreshTokenStore.put(userId, refreshToken);
    }

    // 저장된 토큰 조회(재발급 시 검증용)
    public String findRefreshToken(String userId) {
        return refreshTokenStore.get(userId);
    }

    // 로그아웃 시 토큰 삭제
    public void delete(String userId) {
        refreshTokenStore.remove(userId);
    }
}
