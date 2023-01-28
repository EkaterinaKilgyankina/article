package com.excitement.article.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsCustomService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String basicAuthHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(basicAuthHeader)) {
            try {
                String credentials = new String(Base64.getDecoder().decode(request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1]));
                String[] split = credentials.split(":");
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(split[0]);

                if (passwordEncoder.matches(split[1], userDetails.getPassword())) {
                    final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        filterChain.doFilter(request, response);
    }
}