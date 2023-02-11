package edu.uwp.appfactory.tow.webSecurityConfig.security.jwt;

import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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

    private static final Logger logging = LoggerFactory.getLogger(AuthTokenFilter.class);
    private JwtUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsService;
    private final List<String> bypassUrlFilter = List.of(
            "/api/drivers",
            "/api/auth/refresh",
            "/api/auth/verification",
            "/api/password/forgot",
            "/api/password/forgot/verify",
            "/api/password/forgot/reset",
            "/api/auth/login"
    );



    @Autowired
    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService){
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return bypassUrlFilter.contains(path);
    }

    /**
     * applies a filter to a request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("DINGLEDART");
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String userId = jwtUtils.getUUIDFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUUID(UUID.fromString(userId));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logging.error("Cannot set user authentication: {}", e);
            filterChain.doFilter(request,response);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * method that parses a JWT token from a user
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        //todo: fix JWT to be sent in bearer
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        } else if (StringUtils.hasText(headerAuth)) {
            return headerAuth;
        }
        return null;
    }
}
