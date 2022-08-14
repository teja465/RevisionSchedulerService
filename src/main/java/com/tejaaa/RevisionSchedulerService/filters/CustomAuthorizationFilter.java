package com.tejaaa.RevisionSchedulerService.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mysql.cj.log.Log;
import com.tejaaa.RevisionSchedulerService.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils = new JwtUtils();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("User request url "+request.getRequestURI());
        if (request.getRequestURI().contains("/api/login")){
            //user is trying to login, skip this filter
            log.info("user is trying to login, skip this filter");
            filterChain.doFilter(request,response);
        }
        else {
            String authorizationHeader = request.getHeader("Authorization");
            log.info("verifiying user token "+ authorizationHeader);
            if(authorizationHeader == null){
                log.info("authorizationHeader is null from header");
            }
            try {
                if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                    String jwtToken = authorizationHeader.substring("Bearer ".length());

                    DecodedJWT decodedJWT = jwtUtils.verifyJwtToken(jwtToken);

                    String username = jwtUtils.getUserNameFromDecodedJWT(decodedJWT);
                    String[] roles = decodedJWT.getClaim("claims").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    for (String role : roles) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    log.info("Auth user after verifiying token is is {}",
                            SecurityContextHolder.getContext().getAuthentication().getName());

                    filterChain.doFilter(request,response);
                } else {
                    String message="Didnt have valid Authentication token in header to validate user";
                    log.info(message);
                    response.setHeader("error", "Didnt have valid Authentication token in header");
                    response.setStatus(403);// 403 server refused to autherize
                    response.getWriter().write(message);
                    return;
                }
            } catch (Exception e) {
                log.info("Unknown Exception while verifying   user jwt token ");
                e.printStackTrace();
                response.setHeader("error", "Unknown Exception while verifier  user jwt token ");
                response.setHeader("ErrorStackTrace", e.getLocalizedMessage());
                response.setStatus(403);// 403 server refused to autherize
                return ;
            }
        }

    }
}