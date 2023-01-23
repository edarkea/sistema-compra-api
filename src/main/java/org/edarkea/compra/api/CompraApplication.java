package org.edarkea.compra.api;

import org.edarkea.compra.api.security.JWTAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication
public class CompraApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompraApplication.class, args);
    }

    @EnableWebSecurity
    @Configuration
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().cors().configure(http);
            http.csrf().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/api/test/all").permitAll()
                    .antMatchers(HttpMethod.GET,
                            "/v2/api-docs",
                            "/configuration/ui",
                            "/swagger-resources/**",
                            "/swagger-ui.html",
                            "/webjars/**").permitAll()
                    .anyRequest()
                    .authenticated();
        }
    }

}
