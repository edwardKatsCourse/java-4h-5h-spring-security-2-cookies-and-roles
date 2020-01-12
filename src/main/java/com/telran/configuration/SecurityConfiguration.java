package com.telran.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public SecurityFilter securityFilter() {
        return new SecurityFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
            .antMatchers( "/login", "/cookies-example/*", "/cookies-example", "/secured/cookies", "/secured/cookies/**").permitAll()

                //Role          ADMIN  (ROLE_ADMIN) .startsWith("ROLE_") .endsWith("ADMIN")
                //Authority     ADMIN  (ADMIN)      .equals("ADMIN")

                //get person info

                //update person info

            .antMatchers("/admin/**").hasAuthority("ADMIN")
            .antMatchers(HttpMethod.GET, "/persons/*").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .antMatchers("/**").authenticated()
                .and()
            .logout().disable();

        http.addFilterBefore(securityFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
