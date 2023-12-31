package io.videogamedb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Order(2)
@Service
@EnableWebSecurity
public class WebsiteSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder encoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requiresChannel()
                .anyRequest();
        http
                .authorizeRequests()
                .antMatchers("/cart/view").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/").permitAll();
        http
                .formLogin()
                .loginPage("/login");
        http
                .logout()
                .logoutSuccessUrl("/");
        http
                .exceptionHandling()
                .accessDeniedPage("/");
    }
}
