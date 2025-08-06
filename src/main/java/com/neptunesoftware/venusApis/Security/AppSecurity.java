package com.neptunesoftware.venusApis.Security;

import com.neptunesoftware.venusApis.Beans.AppProps;
import com.neptunesoftware.venusApis.Beans.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class AppSecurity {

    private final AppProps appProps;
    private static final String[] AUTH_WHITELIST = new String[]{"/apis/v2/auth/*","/apis/v2/extras/**", "/apis/v2/alerts-channel/**"};

    public AppSecurity(AppProps appProps) {
        this.appProps = appProps;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers(AUTH_WHITELIST)
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(
                        ex -> ex.authenticationEntryPoint(new AuthProvider()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .sessionManagement(httpSecuritySessionManagementConfigurer
                        -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(appProps.username)
                .password("{noop}" + appProps.password)
                .authorities("USER");
    }

    // ** CORS CONFIGURATION
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> allowedMethods = new ArrayList<>();
        allowedMethods.add("POST");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(allowedMethods);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "content-type"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
