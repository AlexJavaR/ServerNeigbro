package com.prototype.web;

import com.prototype.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = FeedBackRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class FeedBackRestController {
    static final String REST_URL = "/api/v1/feedback";

    @Autowired
    private EmailService emailService;

    @GetMapping()
    public ResponseEntity<Map> sendFeedback(@RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "email", required = false) String email,
                                            @RequestParam(value = "phone", required = false) String phone,
                                            @RequestParam(value = "message", required = false) String message) {
        emailService.sendFeedback(name, email, phone, message);
        Map<String, Integer> response = new HashMap<>();
        response.put("success", 1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
