package com.hitta.AuthTemplate.filter;

import com.hitta.AuthTemplate.service.JwtService;
import com.hitta.AuthTemplate.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Bearer Token
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        try{
            if(authHeader != null && authHeader.startsWith("Bearer")){
                token = authHeader.substring(7);
                username = jwtService.extractUserName(token);
            }

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = context.getBean(UserDetailsServiceImpl.class).loadUserByUsername(username);

                if(jwtService.validateToken(token, userDetails)){
                    var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }catch(ExpiredJwtException expiredJwt){
            System.out.println(expiredJwt.getMessage());
        }


        filterChain.doFilter(request, response);
    }
}
