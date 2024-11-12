package com.example.bibliosys.Configs;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.bibliosys.Services.impl.UserDetailServiceImpl;
import com.example.bibliosys.utils.JwtUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Autowired
        private JwtUtils jwtUtils;

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                        AuthenticationProvider authenticationProvider)
                        throws Exception {
                return httpSecurity
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(http -> {
                                        // EndPoints publicos
                                        http.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll();
                                        http.requestMatchers("/api/email/**").permitAll();

                                        // EndPoints que requieren autenticación con Bearer token
                                        http.requestMatchers("/api/library/**").authenticated();

                                        // EndPoints Privados
                                        http.requestMatchers(HttpMethod.GET, "/method/get").hasAuthority("READ");
                                        http.requestMatchers(HttpMethod.POST, "/method/post").hasAuthority("CREATE");
                                        http.requestMatchers(HttpMethod.DELETE, "/method/delete")
                                                        .hasAuthority("DELETE");
                                        http.requestMatchers(HttpMethod.PUT, "/method/put").hasAuthority("UPDATE");

                                        http.anyRequest().denyAll();
                                })
                                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                                .build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailService) {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setPasswordEncoder(passwordEncoder());
                provider.setUserDetailsService(userDetailService);
                return provider;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
                configuration.setAllowedMethods(Arrays.asList("POST", "PUT", "GET", "DELETE"));
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        FilterRegistrationBean<CorsFilter> corsFilter() {
                FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(
                                new CorsFilter(corsConfigurationSource()));
                corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
                return corsBean;
        }
}