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
import com.phucx.blogapi.model.UserRoleInfoDTO;
import com.phucx.blogapi.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class BasicBlogAuthenticationProvider implements AuthenticationProvider{
    // @Autowired
    // private UserRepository userRepository;
    // @Autowired
    // private UserRoleInfoRepository userRoleInfoRepository;
    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authenticate(authenticatoin={})", authentication);
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        log.info("username: {}, password: {}", username, password);
        if(this.userService.authenticate(username, password)){
            UserRoleInfoDTO userRoleInfoDTO = this.userService.getUserRoles(username);
            Collection<GrantedAuthority> authorities = this.getAuthorities(userRoleInfoDTO);

            log.info("user {} has been logged in", username);
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        throw new UserAuthenticationException("Invalid username or password");
    }

    // private Boolean authenticate(String username, String password) {
    //     User user = userRepository.findByUsername(username).orElseThrow(()-> new UserAuthenticationException("Invalid Username or Password"));
    //     if(passwordEncoder.matches(password, user.getPassword())) return true;
    //     return false;
    // }

    // private UserRoleInfoDTO getUserRoles(String username) {
    //     List<UserRoleInfo> userRoleInfos = userRoleInfoRepository.findByUsername(username);
    //     if(userRoleInfos==null || userRoleInfos.isEmpty()) throw new RuntimeException("User " + username + " does not found");
    //     UserRoleInfo userRoleInfo = userRoleInfos.get(0);
    //     List<String> roles = userRoleInfos.stream().map(UserRoleInfo::getRole).collect(Collectors.toList());
    //     return new UserRoleInfoDTO(userRoleInfo.getId(), userRoleInfo.getUsername(), roles);
    // }


    @Override
    public boolean supports(Class<?> authentication) {
        log.info("authenticatoin class: {}", authentication.getName());
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }

    private Collection<GrantedAuthority> getAuthorities(UserRoleInfoDTO userRoleInfoDTO){
        return userRoleInfoDTO.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());
    }

    // public void setUserRepository(UserRepository userRepository) {
    //     this.userRepository = userRepository;
    // }
    // public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    //     this.passwordEncoder = passwordEncoder;
    // }
    // public void setUserRoleInfoRepository(UserRoleInfoRepository userRoleInfoRepository) {
    //     this.userRoleInfoRepository = userRoleInfoRepository;
    // }
}
