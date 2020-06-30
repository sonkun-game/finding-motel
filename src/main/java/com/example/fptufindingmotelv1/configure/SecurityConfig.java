package com.example.fptufindingmotelv1.configure;

import com.example.fptufindingmotelv1.service.login.JwtAuthenticationFilter;
import com.example.fptufindingmotelv1.service.login.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${ffm.security.config.url.permit-all}")
    private String[] permitAllUrl;

    @Value("${ffm.security.config.url.role-renter}")
    private String[] roleRenterUrl;

    @Value("${ffm.security.config.url.role-admin}")
    private String[] roleAdminUrl;

    @Value("${ffm.security.config.url.role-landlord}")
    private String[] roleLandlordUrl;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers(permitAllUrl)
                .permitAll()
                .antMatchers(roleRenterUrl).access("hasRole('ROLE_RENTER')")
                .antMatchers(roleLandlordUrl).access("hasRole('ROLE_LANDLORD')")
                .antMatchers(roleAdminUrl).access("hasRole('ROLE_ADMIN')")
                .anyRequest()
                .authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
