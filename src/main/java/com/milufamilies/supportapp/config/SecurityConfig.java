package com.milufamilies.supportapp.config;

import com.milufamilies.supportapp.model.enums.Role;
import com.milufamilies.supportapp.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/auth/register/**", "/js/**", "/images/**", "/login", "/register/**", "/error","/families/awaiting-approval").permitAll()
                        .requestMatchers("/offers/**", "/dashboard/**", "/chat/**", "/match/**", "/needs/**").hasAnyRole("VOLUNTEER", "FAMILY")
                        .requestMatchers("/family/**", "/families/**","/families/family_dashboard", "/families/awaiting-approval", "/match/**").hasRole("FAMILY")
                        .requestMatchers("/volunteer/**").hasRole("VOLUNTEER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // עמוד GET
                        .loginProcessingUrl("/login") // טופס POST
                        .successHandler(customSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .build();
    }


    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            var authorities = authentication.getAuthorities();
            String email = authentication.getName();

            com.milufamilies.supportapp.model.User domainUser =
                    userDetailsService.loadUserByEmail(email);

            // ✅ חסימת משפחות לא מאושרות
            if (domainUser.getRole() == Role.ROLE_FAMILY && !domainUser.isApproved()) {
                request.getSession().invalidate();
                response.sendRedirect("/families/awaiting-approval");
                return;
            }

            // ✅ מאושר → שמירת session
            request.getSession().setAttribute("loggedUser", domainUser);

            String redirectUrl = "/";
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_VOLUNTEER"))) {
                redirectUrl = "/volunteer/dashboard";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_FAMILY"))) {
                redirectUrl = "/family/dashboard";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                redirectUrl = "/admin/home";
            }

            response.sendRedirect(redirectUrl);
        };
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService()); // חשוב!
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }
}

