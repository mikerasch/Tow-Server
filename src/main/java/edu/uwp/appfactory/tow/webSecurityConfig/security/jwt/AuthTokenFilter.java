package edu.uwp.appfactory.tow.webSecurityConfig.security.jwt;

import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * class that filters an auth token
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final List<String> bypassUrlFilter = List.of(
            "/api/drivers",
            "/api/auth/refresh",
            "/api/auth/verification",
            "/api/password/forgot",
            "/api/password/forgot/verify",
            "/api/password/forgot/reset",
            "/api/auth/login"
    );

    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService){
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return bypassUrlFilter.contains(path);
    }

    /**
     * applies a filter to a request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }
        String jwtToken = authorizationHeader.substring(7);
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        if(userUUID != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUUID(Long.valueOf(userUUID));
            if(jwtUtils.isTokenValid(jwtToken) && userDetails != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

}
