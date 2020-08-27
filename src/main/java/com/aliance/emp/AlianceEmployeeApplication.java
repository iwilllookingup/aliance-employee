package com.aliance.emp;

import com.aliance.emp.security.JWTAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Chakrit Sereepong
 */
@SpringBootApplication
public class AlianceEmployeeApplication {

  public static void main(String[] args) {
    SpringApplication.run(AlianceEmployeeApplication.class, args);
  }

  /**
   * This is web security configuration to filter with our criteria
   * and permit route that we set
   */
  @EnableWebSecurity
  @Configuration
  class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable()
          .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
          .authorizeRequests()
          .antMatchers(HttpMethod.POST, "/v1/user/login").permitAll()
          .antMatchers(HttpMethod.POST, "/v1/user/register").permitAll()
          .anyRequest().authenticated();
    }
  }

  /**
   * This is bean to perform password encode operation
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
