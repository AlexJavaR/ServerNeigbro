package com.prototype.config;

import com.prototype.security.SecUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
//import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

//@Configuration
//@EnableWebSecurity
//@EnableScheduling
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    SecUserDetailsService userDetailsService;

//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }

//    @Autowired
//    public void configAuthBuilder(AuthenticationManagerBuilder builder) throws Exception {
//        builder.userDetailsService(userDetailsService);
//    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/api/v1/registration");
//        web.ignoring().antMatchers("/api/v1/sale/completed");
//        web.ignoring().antMatchers("/api/v1/sale/denied");
//        web.ignoring().antMatchers("/api/v1/feedback");
//        //web.ignoring().antMatchers("/login");
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS,"/api/v1/**").permitAll()
//                .anyRequest().authenticated()
//                .and().formLogin()
//                .and().rememberMe().rememberMeServices(rememberMeServices())
//                .and().logout().deleteCookies("JSESSIONID").permitAll()
//                //.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                //.and().requestCache()
//                .and().httpBasic();
//        http.csrf().disable();
//    }

//    @Bean
//    RememberMeServices rememberMeServices() {
//        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
//        rememberMeServices.setAlwaysRemember(true);
//        return rememberMeServices;
//    }
}