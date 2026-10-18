package zw.ac.msu.dzimbahub.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration; 
import org.springframework.http.HttpMethod; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity; 
import org.springframework.security.config.http.SessionCreationPolicy; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.security.web.SecurityFilterChain; 
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration 
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Value("${app.cors.allowed-origin:http://localhost:3000}")
    private String allowedOrigin;

    public SecurityConfig(JwtAuthenticationFilter f){
        jwtFilter=f;
    } 

    @Bean 
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    } 

    @Bean 
    SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        return http
            .csrf(c->c.disable())
            // Use our custom CORS configuration
            .cors(c -> c.configurationSource(corsConfigurationSource())) 
            .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a->a
                // 1. Allow CORS preflight requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // 2. Added /api/auth/register just in case your frontend uses that instead of /signup
                .requestMatchers("/api/auth/login","/api/auth/signup", "/api/auth/register", "/api/health","/error").permitAll()
                .requestMatchers("/api/auth/me").authenticated()
                .requestMatchers(HttpMethod.GET,"/api/properties","/api/properties/**","/api/recommendations/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/recommendations").permitAll()
                .requestMatchers("/api/payments/paynow-result").permitAll()
                .requestMatchers("/api/payments/landlord/**").hasRole("LANDLORD")
                .requestMatchers("/api/payments/*/confirm-manual","/api/payments/*/reject-manual").hasRole("LANDLORD")
                .requestMatchers("/api/payments/mine").hasRole("TENANT")
                .requestMatchers("/api/payments/start","/api/payments/*/verify").hasRole("TENANT")
                .requestMatchers("/api/payments/*/receipt").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/landlord/**").hasRole("LANDLORD")
                .requestMatchers("/api/tenant/**").hasRole("TENANT")
                .requestMatchers("/api/viewings/**","/api/messages/**","/api/reviews/**").hasRole("TENANT")
                .anyRequest().denyAll()
            )
            .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    // This Bean is what makes CORS work with Spring Security
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Read from Render environment variable, fallback to localhost
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigin.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}