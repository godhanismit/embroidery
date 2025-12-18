package com.example.demo.config;

import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
//        System.out.println("🔹 JwtFilter: Request URI -> " + request.getRequestURI());
//        System.out.println("🔹 JwtFilter: Authorization header -> " + authHeader);

        String username = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractMobile(token);
//                System.out.println("🔹 JwtFilter: Extracted username -> " + username);
            } catch (Exception e) {
//                System.out.println("⚠️ JwtFilter: Invalid token");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userService.loadUserByUsername(username);
                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                    System.out.println("✅ JwtFilter: Authentication successful for " + username);
                } else {
//                    System.out.println("⚠️ JwtFilter: Token validation failed");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            } catch (UsernameNotFoundException e) {
//                System.out.println("⚠️ JwtFilter: User not found: " + username);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

