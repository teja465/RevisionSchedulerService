package com.tejaaa.RevisionSchedulerService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("api/home")
    public ResponseEntity<String> home(){
        return ResponseEntity.ok().body("hello from revision scheduler");
    }

}
