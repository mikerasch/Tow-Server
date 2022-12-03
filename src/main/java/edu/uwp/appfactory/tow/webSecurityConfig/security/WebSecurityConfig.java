package edu.uwp.appfactory.tow.webSecurityConfig.security;

import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.AuthEntryPointJwt;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.AuthTokenFilter;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * web security configuration class, utilized for authentication,
 * roles, and password encoding. Contains the initial security config
 * method that determines access and authorization
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig{

    UserDetailsServiceImpl userDetailsService;

    private AuthEntryPointJwt unauthorizedHandler;

    private AuthTokenFilter authTokenFilter;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler,AuthTokenFilter authTokenFilter){
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.authTokenFilter = authTokenFilter;
    }

//    @Autowired
//    private RoleHierarchy roleHierarchy;

    public AuthTokenFilter authenticationJwtTokenFilter() {
        return authTokenFilter;
    }

    /**
     * main constructor for the configure object for web security
     *
     * @param authenticationManagerBuilder: builds manager for authentication
     * @throws Exception: any
     */
//    @Autowired
//    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder,AuthenticationProvider authenticationProvider) throws Exception {
//        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
//        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }

    /**
     * encoder for passwords, security
     *
     * @return bcrypt password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * role hierarchy method that pulls from the impl, and sets the hierarchy
     * to what the developer states
     *
     * @return role hierarchy set by the developer
     */
//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        /* tricks lies here */
//        roleHierarchy.setHierarchy("ROLE_SPADMIN > ROLE_ADMIN > ROLE_TCADMIN ROLE_PDADMIN > ROLE_TCUSER ROLE_PDUSER");
//        return roleHierarchy;
//    }

    /**
     * security expression handler, using the default web security expression
     * and the role hierarchy
     *
     * @return default web handler
     */
    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
    //    defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
        return defaultWebSecurityExpressionHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    /**
     * configure method that sets the constraints on accessing the server
     * in accordance with roles, authentication
     *
     * @param http HttpSecurity
     * @throws Exception any
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests().anyRequest().permitAll();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
