package com.phucx.blogapi.authenticationProvider;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.phucx.blogapi.exception.UserAuthenticationException;
import com.phucx.blogapi.model.UserRoles;
import com.phucx.blogapi.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class BasicBlogAuthenticationProvider implements AuthenticationProvider{
    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authenticate(authenticatoin={})", authentication);
        // extracct username and password from users request
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        // authenticate username and password
        if(this.userService.authenticate(username, password)){
            // get user's roles
            UserRoles userRoles = this.userService.getUserRoles(username);
            Collection<GrantedAuthority> authorities = this.getAuthorities(userRoles);
            log.info("user {} has been logged in", username);
            // add username password to session
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        throw new UserAuthenticationException("Invalid username or password");
    }

    // define supported type for authentication provider
    @Override
    public boolean supports(Class<?> authentication) {
        log.info("authenticatoin class: {}", authentication.getName());
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
    // get authories of users
    private Collection<GrantedAuthority> getAuthorities(UserRoles userRoles){
        return userRoles.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());
    }
}
