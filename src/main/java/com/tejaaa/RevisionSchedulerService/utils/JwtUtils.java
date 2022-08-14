package com.tejaaa.RevisionSchedulerService.utils;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;

import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class JwtUtils {

    @Value("${prod.secret}")
    private String secret;
    private final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
    private final int JWT_EXPIRATION_TIME = 1000*10*60*60;
    private final int JWT_REFRESH_EXPIRATION_TIME = 1000*10*60*60*24;



    public String generateToken(UserDetails user,boolean refresh){
        int expTime = refresh ? JWT_EXPIRATION_TIME : JWT_REFRESH_EXPIRATION_TIME;
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() +expTime))
                .withClaim("claims",user.getAuthorities().stream().
                        map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public DecodedJWT verifyJwtToken(String jwtToken){
        JWTVerifier jwtverifier = JWT.require(algorithm).build();
        return jwtverifier.verify(jwtToken);
    }
    public String getUserNameFromDecodedJWT(DecodedJWT token){
        return token.getSubject();
    }



    @Test
    public void verifierTest(){
        String s="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZWphYWFAZ21haWwuY29tIiwiY2xhaW1zIjpbXSwiZXhwIjoxNjYxMzI2MDg5fQ.31ZhxuAxEd1k2YHw7cw3z8AmnquUk-Grree_bfWgCd8";
        DecodedJWT jwt = verifyJwtToken(s);
        log.info("jwt.toString()  {}",jwt.getToken());
        log.info("jwt.getSubject()  {}",jwt.getSubject());

        String  payload=JWT.decode(jwt.getToken()).getPayload();
        log.info("Payload {}",payload);
    }

}
