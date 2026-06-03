package com.franciscoesquivel.dofuschef.security;

import com.franciscoesquivel.dofuschef.auth.CustomUserDetailsService;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer ";
    @Autowired
    private JwtUtils utils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseJwt(request);
            if(token != null && utils.validateToken(token)) {
                final String username = utils.getUserFromToken(token);
                final UserDetails details = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        details,
                        null,
                        details.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if(header != null && header.startsWith(BEARER)) {
            return header.substring(BEARER.length());
        }
        return null;
    }
}
