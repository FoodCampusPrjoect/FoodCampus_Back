        package com.foodcampus.foodcampus.jwt;

        import io.jsonwebtoken.*;

        import jakarta.annotation.PostConstruct;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.stereotype.Component;


        import java.util.Base64;

        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        @Component
        public class JwtTokenProvider {

            @Value("${spring.jpa.properties.jwt.secret-key}")
            private String secretKey;

            private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

            @PostConstruct
            protected void init() {
                secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
            }

            // JWT 액세스 토큰 생성
            public String createAccessToken(String username) {
                String token = createToken(username);
                System.out.println("----------------엑세스 토큰" +token);
                return token;
            }

            // JWT 리프레시 토큰 생성
            public String createRefreshToken(String username) {
                return createToken(username);
            }

            private String createToken(String username) {
                Claims claims = Jwts.claims().setSubject(username);

                return Jwts.builder()
                        .setClaims(claims)
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact();
            }

            // JWT 토큰에서 인증 정보 추출
            public String getEmail(String token) {
                try {
                    return Jwts.parserBuilder()
                            .setSigningKey(secretKey)
                            .setAllowedClockSkewSeconds(600)
                            .build()
                            .parseClaimsJws(token)
                            .getBody()
                            .getSubject();
                } catch (ExpiredJwtException e) {
                    throw new RuntimeException("Expired token");
                }
            }

            // 리프레시 토큰 검증
            public boolean validateRefreshToken(String refreshToken) {
                try {
                    Jwts.parserBuilder()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(refreshToken);
                    return true;
                } catch (JwtException | IllegalArgumentException e) {
                    logger.error("Invalid Refresh Token", e);
                    return false;
                }
            }

        }
