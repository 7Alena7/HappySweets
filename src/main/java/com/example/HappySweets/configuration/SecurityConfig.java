package com.example.HappySweets.configuration;

import com.example.HappySweets.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
   @Autowired
   CustomUserDetailService customUserDetailService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(customUserDetailService); //set the custom user details service
        auth.setPasswordEncoder(bCryptpasswordEncoder()); //set the password encoder - bcrypt
        return auth;
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/resources/**", "/static/**", "/images/**", "/productImages/**", "js/**");
   }
   @Bean
   public static BCryptPasswordEncoder bCryptpasswordEncoder() {
       return new BCryptPasswordEncoder();
   }
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/shop/**", "/register", "/css/**", "/resources/**", "/images/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                ).formLogin(form -> form
                .loginPage("/login").permitAll()
                                .failureUrl("/login?error=true")
                                .defaultSuccessUrl("/")
                                .usernameParameter("email")
                                .passwordParameter("password")
                ).logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .httpBasic(withDefaults());
        return http.build();
    }
}

