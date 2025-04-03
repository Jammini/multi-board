package study.multiproject.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import study.multiproject.config.filter.EmailPasswordAuthFilter;
import study.multiproject.config.filter.JwtAuthorizationFilter;
import study.multiproject.config.handler.CustomLogoutSuccessHandler;
import study.multiproject.config.handler.Http401Handler;
import study.multiproject.config.handler.Http403Handler;
import study.multiproject.config.handler.LoginFailHandler;
import study.multiproject.config.handler.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtTokenUtil jwtTokenUtil;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                            .requestMatchers("/favicon.ico")
                            .requestMatchers("/error")
                            .requestMatchers(toH2Console());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                   .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                                                                     .requestMatchers("/").permitAll()
                                                                     .requestMatchers("/users/signup").permitAll()
                                                                     .requestMatchers("/login").permitAll()
                                                                     .requestMatchers("/posts/*").hasAnyRole("USER", "ADMIN")
//                                                                     .requestMatchers("/admin").hasRole("ADMIN")
                                                                     .anyRequest().authenticated()
                   )
                   .addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                   .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
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
                                                                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                   )
                   .build();
    }

    @Bean
    public EmailPasswordAuthFilter emailPasswordAuthFilter() {
        EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/login", objectMapper, jwtTokenUtil);
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
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
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
