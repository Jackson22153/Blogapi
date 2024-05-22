package com.phucx.blogapi.filter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.phucx.blogapi.authenticationProvider.BasicBlogAuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
// @Component
public class RequestValidationBeforeFilter extends OncePerRequestFilter {

    private BasicBlogAuthenticationProvider basicBlogAuthenticationProvider;

    public void setBasicBlogAuthenticationProvider(BasicBlogAuthenticationProvider basicBlogAuthenticationProvider) {
        this.basicBlogAuthenticationProvider = basicBlogAuthenticationProvider;
    }
    public static final String AUTHENTICATION_SCHEME_BASIC = "Basic";
    private Charset credentialsCharset = StandardCharsets.UTF_8;

    private UsernamePasswordAuthenticationToken converter(HttpServletRequest request, HttpServletResponse response){
        log.info("User is logging");

        String header = request.getHeader(AUTHORIZATION);
        if (header != null) {
            header = header.trim();
            if (StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BASIC)) {
                byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
                byte[] decoded;
                try {
                    decoded = Base64.getDecoder().decode(base64Token);
                    String token = new String(decoded, credentialsCharset);
                    log.info("TOken: {}", token);
                    int delim = token.indexOf(":");
                    if (delim == -1) {
                        throw new BadCredentialsException("Invalid basic authentication token");
                    }else {
                        UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken.unauthenticated(token.substring(0, delim), token.substring(delim + 1));

                        return result;
                    }
                } catch (IllegalArgumentException e) {
                    throw new BadCredentialsException("Failed to decode basic authentication token");
                }
            }
        }
        return null;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = this.converter(request, response);
            Authentication authentication = this.basicBlogAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);
            log.info("Authenticatoin: {}", authentication);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

                
            filterChain.doFilter(request, response);
    }


    // @Override
    // protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    //     String uri = request.getRequestURI();
    //     String[] path = uri.split("/");
    //     log.info("isEqual: {}", path[1].equalsIgnoreCase("login"));
    //     return !(path[1].equalsIgnoreCase("login"));
    // }

}
