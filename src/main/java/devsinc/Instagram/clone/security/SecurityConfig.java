package devsinc.Instagram.clone.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    /**
     * Configures and returns a custom SecurityFilterChain for handling authentication and authorization within the application.
     *
     * @param httpSecurity The HttpSecurity configuration to customize security settings.
     * @return A SecurityFilterChain that defines security rules and filters for different URL patterns.
     * @throws Exception If there's an issue configuring security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{
                    auth.requestMatchers(
                            "/users/signup",
                            "/users/login"
                            ,"/v2/api-docs"
                            ,"/v3/api-docs"
                            ,"/v3/api-docs/**"
                            ,"/swagger-resources"
                            ,"/swagger-resources/**"
                            ,"/configuration/ui"
                            ,"/configuration/security"
                            ,"/swagger-ui/**"
                            ,"/webjars/**"
                            ,"/swagger-ui.html").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
