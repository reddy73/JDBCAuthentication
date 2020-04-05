package com.myapp.authenticateservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Value("${user.details.fetch.query}")
    private String userDetailsQuery;

    @Value("${user.details.fetch.role.query}")
    private String defaultRoleQuery;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery(userDetailsQuery)
                .authoritiesByUsernameQuery(defaultRoleQuery);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    		 http.headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers("/h2-console/**","/actuator/health","/swagger-ui**","/api/docs")
                .permitAll()
                .antMatchers(HttpMethod.POST,"/user-management/users")
                .authenticated()
                .and().csrf().disable()
                .httpBasic();
    }

}
