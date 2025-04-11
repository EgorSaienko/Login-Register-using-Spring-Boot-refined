package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Обробник успішної аутентифікації.
 * Переадресовує користувача на відповідну сторінку залежно від його ролі.
 */
@Configuration
public class CustomLoginSucessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * Обробляє редірект після успішного входу в систему.
     *
     * @param request         HTTP-запит
     * @param response        HTTP-відповідь
     * @param authentication  Об'єкт аутентифікації з інформацією про користувача
     * @throws IOException    У випадку помилки вводу/виводу
     */
    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        if (response.isCommitted()) return;
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    /**
     * Визначає URL для перенаправлення після входу на основі ролей користувача.
     *
     * @param authentication Об'єкт аутентифікації
     * @return URL для редіректу
     */
    protected String determineTargetUrl(Authentication authentication) {
        String url = "/login?error=true";
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }
        if (roles.contains("ADMIN")) {
            url = "/admin/dashboard";
        } else if (roles.contains("USER")) {
            url = "/dashboard";
        }
        return url;
    }
}
