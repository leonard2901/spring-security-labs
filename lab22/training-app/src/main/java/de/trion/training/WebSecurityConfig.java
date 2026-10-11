package de.trion.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.health.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig
{
    @Profile("!flyway")
    @Configuration
    public static class JdbcMigrationConfiguration {
        @Autowired
        public void configureAuthentication(AuthenticationManagerBuilder builder, DataSource dataSource) throws Exception {
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

            UserDetails actuator =
               User.withUsername("actuator")
                  .password("{noop}actuator")
                  .roles("ENDPOINT_ADMIN")
                  .build();

            builder
                .jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema()
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder())
                .withUser(user).withUser(user2).withUser(user3).withUser(actuator);
        }
    }

    @Profile("flyway")
    @Configuration
    public static class FlywayMigrationConfiguration {
        @Bean
        UserDetailsManager userDetailsManager(DataSource dataSource) {
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

            UserDetails actuator =
               User.withUsername("actuator")
                  .password("{noop}actuator")
                  .roles("ENDPOINT_ADMIN")
                  .build();


            var manager = new JdbcUserDetailsManager(dataSource);
            manager.createUser(user);
            manager.createUser(user2);
            manager.createUser(user3);
            manager.createUser(actuator);
            return manager;
        }
    }


    @Bean
    public SecurityFilterChain endpoint(HttpSecurity http) throws Exception {
        return http.securityMatcher(EndpointRequest.toAnyEndpoint().excluding(HealthEndpoint.class))
                .authorizeHttpRequests(a -> a
                        .anyRequest().hasRole("ENDPOINT_ADMIN"))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Profile("dev")
    @Bean
    public SecurityFilterChain h2FilterChain(HttpSecurity http) throws Exception {
        return http.securityMatcher(PathRequest.toH2Console())
           .authorizeHttpRequests(a -> a
              .anyRequest()
              .permitAll())
            .csrf(AbstractHttpConfigurer::disable)
            .headers(h -> h
               .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .build();
    }

    @Bean
    public SecurityFilterChain app(HttpSecurity httpSecurity) throws Exception {
       return httpSecurity
           .securityMatcher("/**")
           .authorizeHttpRequests(a -> a
              .requestMatchers(HttpMethod.POST, "/trainings/**").hasRole("ADMIN")
              .requestMatchers("/trainings/*/edit").hasRole("ADMIN")
              .anyRequest().permitAll()
           )
           .formLogin(Customizer.withDefaults())
           .logout(l -> l.logoutSuccessUrl("/"))
           .httpBasic(Customizer.withDefaults())
           .csrf(c -> c.ignoringRequestMatchers("/api/**"))
           .anonymous(a -> a.principal(new User("anonymous","", Collections.emptyList())))
          .build();
    }
}
