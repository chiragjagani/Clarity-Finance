package com.cj.financeapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = getTokenFromRequest(request);
        String requestPath = request.getRequestURI();

        if (StringUtils.hasText(jwt)) {
            log.info("JWT token found in request header for path: {}", requestPath);

            if (jwtUtil.validateToken(jwt)) {
                log.info("JWT token validated successfully");

                try {
                    String userId = jwtUtil.getUserIdFromJWT(jwt);
                    log.info("Extracted userId from JWT: {}", userId);

                    UserDetails userDetails = userDetailsService.loadUserById(Long.parseLong(userId));
                    log.info("Loaded user details for userId: {}", userId);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Authentication set in SecurityContext for userId: {}", userId);
                } catch (Exception ex) {
                    log.warn("Error setting user authentication: {}", ex.getMessage(), ex);
                }

            } else {
                log.warn("JWT validation failed for path: {}", requestPath);
            }
        } else {
            log.debug("No JWT token found in request header for path: {}", requestPath);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            log.debug("Bearer token found: {}", bearerToken);
            return bearerToken.substring(7);
        }
        return null;
    }
}
