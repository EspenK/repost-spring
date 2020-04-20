package me.kverna.spring.repost.security;

import java.util.Arrays;
import me.kverna.spring.repost.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final RepostUserDetailsService userDetailsService;
    private final UserDetailsAuthenticationProvider userDetailsAuthenticationProvider;
    private final Config config;
    private final Logger logger;

    @Autowired
    public WebSecurityConfig(RepostUserDetailsService userDetailsService,
            UserDetailsAuthenticationProvider userDetailsAuthenticationProvider, Config config) {
        this.userDetailsService = userDetailsService;
        this.userDetailsAuthenticationProvider = userDetailsAuthenticationProvider;
        this.config = config;
        this.logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(userDetailsAuthenticationProvider);
    }

    @Bean
    public static CorsConfigurationSource corsConfigurationSource() {
        String[] origins = {
                "http://localhost",
                "http://localhost:8080"
        };

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.applyPermitDefaultValues();
        configuration.setAllowedOrigins(Arrays.asList(origins));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        // TODO: implement random key generation
        String signingKey = config.getSigningKey();
        if (signingKey == null) {
            logger.error("Could not find a signing key in the application properties. "
                    + "Please provide a signing key with the repost.signing-key property.");
            signingKey = "unsafe_signing_key_which_should_not_be_used";
        }

        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(signingKey);
        return jwtAccessTokenConverter;
    }
}
