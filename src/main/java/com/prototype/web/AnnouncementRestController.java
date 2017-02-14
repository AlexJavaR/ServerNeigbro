package com.prototype.web;

import com.prototype.model.event.announcement.AnnouncementEvent;
import com.prototype.model.event.announcement.UserAnnouncementEvent;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.announcement.AnnouncementService;
import com.prototype.to.UserAnnouncement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = AnnouncementRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AnnouncementRestController {
    static final String REST_URL = "/api/v1";

    @Autowired
    private AnnouncementService eventService;

    @PostMapping(value = "/me/announcement", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnnouncementEvent> addAnnouncement(@RequestBody UserAnnouncement announcement) {
        BigInteger userId = AuthorizedUser.id();
        AnnouncementEvent currentAnnouncement = eventService.addAnnouncement(announcement, userId);
        return new ResponseEntity<>(currentAnnouncement, HttpStatus.CREATED);
    }

    @GetMapping(value = "/me/announcement/{addressId}")
    public ResponseEntity<List<UserAnnouncementEvent>> findAllAnnouncementsOfAddress(@PathVariable("addressId") BigInteger addressId) {
        List<UserAnnouncementEvent> announcementEventList = eventService.findAllAnnouncementsOfAddress(addressId);
        return new ResponseEntity<>(announcementEventList, HttpStatus.OK);
    }
}
