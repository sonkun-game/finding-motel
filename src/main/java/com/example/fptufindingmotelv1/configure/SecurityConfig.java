package com.example.fptufindingmotelv1.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/","/trang-chu", "/assets/**", "/custom-assets/**", "/dang-nhap", "/custom-assets/**", "/dang-ki", "/custom-assets/**", "/dang-ki-with-gg")
                .permitAll()
                .anyRequest()
                .authenticated();
    }
}
