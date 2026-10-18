package zw.ac.msu.dzimbahub.security;

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

@Configuration 
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

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
            .cors(c -> c.configurationSource(corsConfigurationSource())) // Apply CORS here
            .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a->a
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow preflight
                .requestMatchers("/api/auth/login","/api/auth/signup", "/api/health","/error").permitAll()
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // HARDCODED your Vercel URL for testing
        configuration.setAllowedOrigins(Arrays.asList("https://dzimba-hub-frontend.vercel.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source; // IMPORTANT: Return source, not configuration
    }
}