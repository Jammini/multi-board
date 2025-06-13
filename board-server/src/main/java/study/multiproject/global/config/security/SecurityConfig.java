package study.multiproject.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue;
import study.multiproject.global.config.security.filter.EmailPasswordAuthFilter;
import study.multiproject.global.config.security.filter.JwtAuthorizationFilter;
import study.multiproject.global.config.security.handler.CustomLogoutSuccessHandler;
import study.multiproject.global.config.security.handler.Http401Handler;
import study.multiproject.global.config.security.handler.Http403Handler;
import study.multiproject.global.config.security.handler.LoginFailHandler;
import study.multiproject.global.config.security.handler.LoginSuccessHandler;
import study.multiproject.global.util.JwtTokenUtil;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                            .requestMatchers("/favicon.ico")
                            .requestMatchers("/error")
                            .requestMatchers("/h2-console/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                   .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                                                                     .requestMatchers("/")
                                                                     .permitAll()
                                                                     .requestMatchers(
                                                                         "/users/signup")
                                                                     .permitAll()
                                                                     .requestMatchers("/login")
                                                                     .permitAll()
                                                                     .requestMatchers( "/shortenUrl/**")
                                                                     .permitAll()
                                                                     .requestMatchers( "/{shortenUrlKey}")
                                                                     .permitAll()
                                                                     .requestMatchers("/auth/**")
                                                                     .permitAll()
                                                                     .requestMatchers("/files/preview/**")
                                                                     .permitAll()
                                                                     .anyRequest().authenticated()
                   )
                   .addFilterBefore(emailPasswordAuthFilter(),
                       UsernamePasswordAuthenticationFilter.class)
                   .addFilterBefore(jwtAuthorizationFilter,
                       UsernamePasswordAuthenticationFilter.class)
                   .exceptionHandling(e -> {
                       e.accessDeniedHandler(new Http403Handler(objectMapper));
                       e.authenticationEntryPoint(new Http401Handler(objectMapper));
                   })
                   .logout(logout -> logout
                                         .logoutUrl("/logout") // POST 요청으로 호출
                                         .logoutSuccessHandler(logoutSuccessHandler())
                   )
                   .csrf(AbstractHttpConfigurer::disable)
                   .sessionManagement((sessionManagement) -> sessionManagement
                                                                 .sessionCreationPolicy(
                                                                     SessionCreationPolicy.STATELESS)
                   )
                   .headers(headers -> headers.xssProtection(xss -> xss.headerValue(
                       HeaderValue.ENABLED_MODE_BLOCK)).contentSecurityPolicy(
                       cps -> cps.policyDirectives("script-src 'self'")))
                   .build();
    }

    @Bean
    public EmailPasswordAuthFilter emailPasswordAuthFilter() {
        EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/login", objectMapper,
            jwtTokenUtil);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(loginSuccessHandler());
        filter.setAuthenticationFailureHandler(loginFailHandler());
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginFailHandler loginFailHandler() {
        return new LoginFailHandler(objectMapper);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public CustomLogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }
}
