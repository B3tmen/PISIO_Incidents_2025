package org.unibl.etf.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gateway/api/v1/health")
public class TestController {

    @GetMapping
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Service is up");
    }
}
