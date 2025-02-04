package com.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@EnableMethodSecurity
public class SpringSecurityConfig {
    /*
      When we changed auth from "Form Based Config" to basic auth (pop up) we lost the privilege of using bcrypt implicitly,
      so we have to create this bean to use it explicitly in our code
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
      This class is used to override spring form based config to basic config
      we have to create a bean that return securityFilterChain to modify form based security
      it takes HttpSecurity and method argument which is a class itself and this must throw exception
      csrf is an attack prevention technique provided by spring boot we need to disable it for now
      we need to implement the following lambda expression to apply this filter on every coming request
      and this " .httpBasic(Customizer.withDefaults()); " that apply the  basic auth on all the upcoming requests
      we must choose the one that takes customizer as argument and pass customizer.with defaults as argument
      basic authentication is pop up not form
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF (ensure it's safe in your context)
                .authorizeHttpRequests(authorize ->
                        authorize
                                // Allow all authenticated users to perform GET and PATCH requests
//                                .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
//                                .requestMatchers(HttpMethod.PATCH, "/api/**").authenticated()
//
//                                // Restrict POST and PUT requests to users with the "ADMIN" role
//                                .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/**").permitAll()

                                // Any other request needs to be authenticated
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // Basic Authentication for simplicity

        return http.build();
    }

    /*
    This bean is specially created to have in memory object to store our users data and define their roles
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails hassan = User.builder().
                username("admin")
                .password(passwordEncoder().encode("password")).
                roles("ADMIN").build();
        UserDetails user2 = User.builder()
                .username("user").
                password(passwordEncoder().encode("password"))
                .roles("USER").build();
        return new InMemoryUserDetailsManager(hassan, user2);
    }
}
