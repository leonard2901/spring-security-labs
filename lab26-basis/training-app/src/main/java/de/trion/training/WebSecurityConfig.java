package de.trion.training;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity

                // Which requests should be protected by this filter chain
                .securityMatcher("/api/**")

                // Authentication
                // TODO: .oauth2ResourceServer(o -> ...)

                // Authorization
                // TODO: .authorizeHttpRequests(a -> ...)

                // Infrastructure/Defense
                // TODO: disable CSRF protection
                // TODO: stateless session management

                .build();
    }

    @Profile("dev")
    @Bean
    public SecurityFilterChain h2SecurityFilterChain(HttpSecurity http) throws Exception {
        return http.securityMatcher(PathRequest.toH2Console())
                .authorizeHttpRequests(a -> a
                        .anyRequest()
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(h -> h
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .build();
    }
}
