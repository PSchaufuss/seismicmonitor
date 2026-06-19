package dk.ek.seismicmonitor.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/error").permitAll()
                        .requestMatchers("/*.css", "/*.js").permitAll()
                        .requestMatchers("/api/health").permitAll()

                        .requestMatchers("/earthquake-alerts.html").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/sensor-data.html").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/sensor-data").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/earthquake-alerts/active").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/earthquake-alerts/*/user-reports").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/api/sensor-data").hasRole("ADMIN")
                        .requestMatchers("/api/sensor-data/**").hasRole("ADMIN")

                        .requestMatchers("/api/earthquake-alerts").hasRole("ADMIN")
                        .requestMatchers("/api/earthquake-alerts/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.withUsername("user")
                .password("{noop}user123")
                .roles("USER")
                .build();

        var admin = User.withUsername("admin")
                .password("{noop}admin123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}