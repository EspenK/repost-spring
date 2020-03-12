package me.kverna.spring.repost.config;

import me.kverna.spring.repost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class RepostUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final Set<GrantedAuthority> defaultAuthorities;

    @Autowired
    public RepostUserDetailsService(UserService userService) {
        this.userService = userService;
        this.defaultAuthorities = new HashSet<>();
        this.defaultAuthorities.add(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        me.kverna.spring.repost.data.User user = userService.getUser(username);
        return new User(user.getUsername(), user.getHashedPassword(), defaultAuthorities);
    }
}
