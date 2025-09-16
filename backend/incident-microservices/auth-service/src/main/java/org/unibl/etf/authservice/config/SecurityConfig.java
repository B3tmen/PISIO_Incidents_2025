package org.unibl.etf.authservice.config;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig {


    //@Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> {
//                            auth.requestMatchers("/**").permitAll();
//                            auth.anyRequest().permitAll();
//                })
//                .oauth2Login(oauth2 -> oauth2
//                        .defaultSuccessUrl("http://localhost:3000/moderation", true)
//                )
//                //.formLogin(Customizer.withDefaults())
//        ;
//
//        return http.build();
//    }
}
