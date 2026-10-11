package de.trion.training;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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
           .build();

        UserDetails user2 =
           User.withUsername("thomas")
              .password("{noop}password") // Gehashtes Password mit "kein"-Hashing
              .build();

        UserDetails user3 =
           User.withUsername("trainer")
              .password("{bcrypt}$2a$10$TeUIcgYkrlr.8AgpgWOOKe1LqqD6ThV9w59VTxxCiaz3Y1FZah8Sq")
              .build();

        return new InMemoryUserDetailsManager(user, user2, user3);
    }
}
