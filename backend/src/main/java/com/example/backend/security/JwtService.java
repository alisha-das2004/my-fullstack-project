package com.example.backend.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
	private final SecretKey secretKey;
	private final long expirationTime = 15*60*1000;
	
	public JwtService() {
		String secret = System.getenv("JWT_SECRET");
		
		if(secret == null || secret.length()<32) {
			throw new IllegalStateException("Set JWT_SECRET to a secret of at least 32 characters");
		}
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
	}
	public String generateToken(String email,String role) {
		Date now = new Date();
		Date expiry  = new Date(now.getTime() + expirationTime);
		
		return Jwts.builder()
				.subject(email)
				.claim("role", role)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(secretKey)
				.compact();
	}
	public Claims extractClaims(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	public boolean isTokenValid(String token) {
		try {
			Claims claims = extractClaims(token);
			return claims.getSubject()!=null
					&& claims.getExpiration().after(new Date());
			
			
		} catch (Exception exception) {
			return false;
		}
	}
	
}
