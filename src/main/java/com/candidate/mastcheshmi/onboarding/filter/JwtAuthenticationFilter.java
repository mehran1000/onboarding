package com.candidate.mastcheshmi.onboarding.filter;

import com.candidate.mastcheshmi.onboarding.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This filter intercepts incoming HTTP requests and checks for a JWT (JSON Web Token) in the "Authorization" header. If
 * a valid JWT is found, it extracts the authentication information from the token and stores it in
 * SecurityContextHolder.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String header = request.getHeader(HEADER_STRING);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            String token = header.replace(TOKEN_PREFIX, "");
            Authentication authentication = tokenService.getAuthentication(token);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
