package zw.ac.msu.dzimbahub.security;
import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.http.HttpMethod; import org.springframework.security.config.Customizer; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.http.SessionCreationPolicy; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.SecurityFilterChain; import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration public class SecurityConfig {private final JwtAuthenticationFilter jwtFilter;public SecurityConfig(JwtAuthenticationFilter f){jwtFilter=f;} @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();} @Bean SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{return http.csrf(c->c.disable()).cors(Customizer.withDefaults()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a
.requestMatchers("/api/auth/login","/api/auth/signup","/api/health","/error").permitAll()
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
.anyRequest().denyAll()).addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class).build();}}
