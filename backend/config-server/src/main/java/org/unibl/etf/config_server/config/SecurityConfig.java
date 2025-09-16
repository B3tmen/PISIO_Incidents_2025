package org.unibl.etf.config_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] WHITELIST_URLS = {
            "/actuator/health",
            "/**",
            "/actuator/busrefresh",
    };
    private static final String[] ACTUATOR_BLACKLIST = {

    };

    private final GithubWebhookFilter webhookFilter;

    @Autowired
    public SecurityConfig(GithubWebhookFilter webhookFilter) {
        this.webhookFilter = webhookFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(webhookFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITELIST_URLS).permitAll()
                        .requestMatchers(ACTUATOR_BLACKLIST).authenticated()
                        .anyRequest().authenticated()
                );
                //.httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
