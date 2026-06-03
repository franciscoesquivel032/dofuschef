package com.franciscoesquivel.dofuschef.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private IUserRepository repo;

    @Autowired
    public CustomUserDetailsService(IUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = repo.findUserByUsername(username);
        if(u == null) throw new UsernameNotFoundException("User not found with username: " + username);
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                Collections.emptyList()
        );
    }
}
