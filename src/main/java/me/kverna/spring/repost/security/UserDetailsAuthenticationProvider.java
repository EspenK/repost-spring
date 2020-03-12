package me.kverna.spring.repost.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final RepostUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsAuthenticationProvider(RepostUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
        if (token.getCredentials() == null) {
            throw new BadCredentialsException("Invalid login information");
        }

        String password = (String) token.getCredentials();
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid login information");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
        return userDetailsService.loadUserByUsername(username);
    }
}
