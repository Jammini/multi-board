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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import study.multiproject.config.filter.EmailPasswordAuthFilter;
import study.multiproject.config.handler.CustomLogoutSuccessHandler;
import study.multiproject.config.handler.Http401Handler;
import study.multiproject.config.handler.Http403Handler;
import study.multiproject.config.handler.LoginFailHandler;
import study.multiproject.config.handler.LoginSuccessHandler;
import study.multiproject.domain.user.User;
import study.multiproject.domain.user.UserRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

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
                                                                     .anyRequest().permitAll()
                   )
                   .addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                   .exceptionHandling(e -> {
                            e.accessDeniedHandler(new Http403Handler(objectMapper));
                            e.authenticationEntryPoint(new Http401Handler(objectMapper));
                     })
                   .logout(logout -> logout
                                         .logoutUrl("/logout") // POST 요청으로 호출
                                         .logoutSuccessHandler(logoutSuccessHandler())
                                         .invalidateHttpSession(true)
                                         .deleteCookies("JSESSIONID")
                   )
                   .rememberMe(rm -> rm
                                         .rememberMeParameter("remember")
                                         .alwaysRemember(false)
                                         .tokenValiditySeconds(2592000))
                   .csrf(AbstractHttpConfigurer::disable)
                   .build();
    }

    @Bean
    public EmailPasswordAuthFilter emailPasswordAuthFilter() {
        EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/login", objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(loginSuccessHandler());
        filter.setAuthenticationFailureHandler(loginFailHandler());
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        filter.setRememberMeServices(rememberMeServices());
        return filter;
    }

    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setValiditySeconds(3600 * 24 * 30);
        return rememberMeServices;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService(userRepository));
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> {
            User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email + "을 찾을 수 없습니다."));
            return new UserPrincipal(user);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
