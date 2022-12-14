package com.tejaaa.RevisionSchedulerService.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.io.CharStreams;
import com.tejaaa.RevisionSchedulerService.entitys.AppUser;
import com.tejaaa.RevisionSchedulerService.service.AppUserService;
import com.tejaaa.RevisionSchedulerService.utils.JwtUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;


@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final  AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils = new JwtUtils();

    private String userName;


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager=authenticationManager;
    }
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        userName =username;
        String password = request.getParameter("password");
        log.info("CustomAuthenticationFilter,Logging in user : Username {} password {} ",username,password);

        if ((username == null) || (password == null)){
            String message="Username or password is null";
            log.info(" {} {} {}",message,username,password);

            response.getWriter().write(message);
            return null;
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
        log.info("Got token {}",token);
        log.info("user auth status is {} ",token.isAuthenticated());
        Authentication auth =  authenticationManager.authenticate(token);
        log.info("details {}",auth);
        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("in successfulAuthentication () filter ");
        User user= (User) authentication.getPrincipal();
        String jwt_token = jwtUtils.generateToken(user,false);

        String refresh_token = jwtUtils.generateToken(user,true);
        response.setHeader("access_token",jwt_token);
        response.setHeader("refresh_token",refresh_token);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Access-Control-Allow-Origin","*");
        HashMap<String,String> body =new HashMap<>();
        body.put("access_token",jwt_token);
        body.put("refresh_token",refresh_token);
        JSONObject json =  new JSONObject(body);
        response.getWriter().write(json.toString());
        return;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info("in unsuccessfulAuthentication");
        String msg = String.format("user %s is either disabled or not found in database",userName);

        HashMap<String,String> body =new HashMap<>();
        body.put("error",msg);
        JSONObject json =  new JSONObject(body);
        response.getWriter().write(json.toString());
        return;
    }
}