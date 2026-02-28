package madp.user.global.configuration;

import lombok.RequiredArgsConstructor;
import madp.user.global.enums.Role;
import madp.user.global.filter.MadpUserInfoExtractorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private static final String[] excludedPaths = {"/actuator/health"};

    @Bean
    public PathMatcher pathMatcher() {return new AntPathMatcher();}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .anonymous(anonymous -> anonymous
                        .principal(Role.GUEST.name())
                        .authorities(Role.GUEST.getValue())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/user/auth-status").permitAll()
                        .requestMatchers("/user/profile/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user/withdraw").hasAnyRole("USER", "ADMIN") 
                        .requestMatchers("/user/deactivate").hasRole("ADMIN")
                        .anyRequest().denyAll()
                )
                .addFilterAfter(new MadpUserInfoExtractorFilter(pathMatcher(), excludedPaths), SecurityContextHolderFilter.class);

        return http.build();
    }
}
