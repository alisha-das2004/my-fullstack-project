package com.example.backend.security;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
            
	  private final JwtService jwtService;

	  public JwtAuthenticationFilter(JwtService jwtService) {
		
		this.jwtService = jwtService;
	  }
	  
	  @Override
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
		  
		  try {
			
			  if(jwtService.isTokenValid(token) && SecurityContextHolder.getContext()
					  .getAuthentication()==null) {
				  Claims claims = jwtService.extractClaims(token);
				  
				  String email = claims.getSubject();
				  String role = claims.get("role",String.class);
			  if(email != null && role!= null && (role.equals("CUSTOMER") || role.equals("ADMIN"))) {
				  UsernamePasswordAuthenticationToken authentication 
				  = new UsernamePasswordAuthenticationToken(email,
						  null,
						  List.of(new SimpleGrantedAuthority("ROLE_"+role)));
				  authentication.setDetails(new WebAuthenticationDetailsSource()
						  .buildDetails(request));
				  
				  SecurityContextHolder.getContext().setAuthentication(authentication);
				  
			  }
			  }
		} catch (Exception exception) {
			SecurityContextHolder.clearContext();
		}
		  filterChain.doFilter(request, response);
	  }
	  
	  
}
