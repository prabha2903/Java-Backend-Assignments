package com.prabha.jwt_authentication.security;

import com.prabha.jwt_authentication.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Intercepts every request once, extracts a Bearer JWT from the Authorization header
 * (if present), validates it, and, if valid, populates the SecurityContext so that
 * downstream authorization checks (e.g. .anyRequest().authenticated()) succeed.
 *
 * If the header is missing entirely, the filter simply passes the request through
 * unauthenticated; the security config's authorization rules and entry point take it
 * from there. If the token is present but invalid/expired, a descriptive message is
 * stashed as a request attribute for {@link JwtAuthenticationEntryPoint} to surface.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTH_HEADER);
        String username = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            token = authHeader.substring(BEARER_PREFIX.length());
            try {
                username = jwtUtil.extractUsername(token);
            } catch (ExpiredJwtException ex) {
                request.setAttribute("jwt_exception", "JWT token has expired");
            } catch (JwtException | IllegalArgumentException ex) {
                request.setAttribute("jwt_exception", "Invalid JWT token");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    request.setAttribute("jwt_exception", "Invalid JWT token");
                }
            } catch (UsernameNotFoundException ex) {
                request.setAttribute("jwt_exception", "User associated with this token no longer exists");
            }
        }

        filterChain.doFilter(request, response);
    }
}
