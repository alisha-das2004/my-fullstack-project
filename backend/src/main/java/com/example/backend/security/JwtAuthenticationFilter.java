package com.example.backend.security;

import java.io.IOException;
import java.net.http.HttpRequest;

import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter {
            
	  private final JwtService jwtService;

	  public JwtAuthenticationFilter(JwtService jwtService) {
		
		this.jwtService = jwtService;
	  }
	  
	  protected void doFilterInternal(HttpServletRequest request ,
			  HttpServletResponse response, 
			  FilterChain filterChain) throws IOException,ServletException{
		  
		  String token = null;
		  
		  Cookie[] cookies = request.getCookies();
		  
		  if(cookies != null) {
			  for(Cookie cookie : cookies) {
				  if("accessToken".equals(cookie.getName())) {
					  token = cookie.getValue();
					  break;
				  }
			  }
		  }
		  if(token == null || token.isEmpty()) {
			  filterChain.doFilter(request, response);
			  return;
		  }
	  }
	  
	  
}
