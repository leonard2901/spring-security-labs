package de.trion.training;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig
{

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails user =
           User.withDefaultPasswordEncoder() // nicht in Produktion nutzen
           .username("user")
           .password("password") //hard coded Klartext-Password
           .roles("USER")
           .build();

        UserDetails user2 =
           User.withUsername("thomas")
              .password("{noop}password") // Gehashtes Password mit "kein"-Hashing
              .roles("USER")
              .build();

        UserDetails user3 =
           User.withUsername("trainer")
              .password("{bcrypt}$2a$10$TeUIcgYkrlr.8AgpgWOOKe1LqqD6ThV9w59VTxxCiaz3Y1FZah8Sq")
              .roles("ADMIN")
              .build();

        return new InMemoryUserDetailsManager(user, user2, user3);
    }

    @Profile("dev")
    @Bean
    public SecurityFilterChain h2FilterChain(HttpSecurity http) throws Exception {
        return
        http.securityMatcher(PathRequest.toH2Console())
           .authorizeHttpRequests(a -> a
              .anyRequest()
              .permitAll())
            .csrf(AbstractHttpConfigurer::disable)
            .build();
    }
}
