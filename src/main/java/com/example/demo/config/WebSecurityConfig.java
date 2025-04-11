package com.example.demo.config;

import com.example.demo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Конфігурація безпеки для веб-додатку.
 * Визначає правила доступу, аутентифікацію та авторизацію.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomLoginSucessHandler sucessHandler;

    /**
     * Сервіс для завантаження користувачів.
     *
     * @return реалізація UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    /**
     * Шифрувальник паролів з використанням BCrypt.
     *
     * @return енкодер
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Менеджер аутентифікації.
     *
     * @param authConfig конфігурація аутентифікації
     * @return менеджер
     * @throws Exception у разі помилки
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Провайдер аутентифікації, що використовує базу даних.
     *
     * @return налаштований провайдер
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Основний ланцюг фільтрів безпеки, де задаються всі правила доступу.
     *
     * @param http об'єкт HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception у разі помилки
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/", "/login", "/register").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers("/account/**").hasAnyAuthority("USER")
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .successHandler(sucessHandler)
                .usernameParameter("email")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");

        http.authenticationProvider(authenticationProvider());
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }

    /**
     * Ігнорує певні шляхи (наприклад, ресурси), щоб уникнути перевірки безпеки.
     *
     * @return кастомізатор безпеки
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
