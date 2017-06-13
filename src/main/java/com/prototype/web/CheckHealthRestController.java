package com.prototype.web;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = CheckHealthRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class CheckHealthRestController {
    static final String REST_URL = "/api/health";

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping()
    public ResponseEntity<Void> checkHealthDatabase() {
        DBObject ping = new BasicDBObject("ping", "1");
        CommandResult command = mongoTemplate.getDb().command(ping);

        if (command.ok()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
