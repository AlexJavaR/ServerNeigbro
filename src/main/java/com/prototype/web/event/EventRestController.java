package com.prototype.web.event;

import com.prototype.model.event.Event;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = EventRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class EventRestController {
    static final String REST_URL = "/api/v1";

    @Autowired
    private EventService eventService;

    @GetMapping(value = "/me/event/{addressId}/{apartment}")
    public ResponseEntity<List<Event>> findGeneralEventsAsHousemate(@PathVariable("addressId") BigInteger addressId,
                                                                  @PathVariable("apartment") String apartment) {
        if (addressId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        BigInteger userId = AuthorizedUser.id();
        List<Event> generalEventList = eventService.findGeneralEventsAsHousemate(addressId, apartment, userId);
        if (generalEventList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(generalEventList, HttpStatus.OK);
    }

    @GetMapping(value = "/event/{addressId}")
    public ResponseEntity<List<Event>> findGeneralEventsAsManager(@PathVariable("addressId") BigInteger addressId) {
        if (addressId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        BigInteger userId = AuthorizedUser.id();
        List<Event> generalEventList = eventService.findGeneralEventsAsManager(addressId, userId);
        if (generalEventList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(generalEventList, HttpStatus.OK);
    }
}
