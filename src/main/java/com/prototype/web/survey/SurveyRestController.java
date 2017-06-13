package com.prototype.web.survey;

import com.prototype.model.event.survey.SurveyEvent;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.survey.SurveyService;
import com.prototype.to.survey.SingleSurveyEvent;
import com.prototype.to.survey.SingleSurveyEventById;
import com.prototype.to.survey.SingleVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = SurveyRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class SurveyRestController {
    static final String REST_URL = "/api/v1/survey";

    @Autowired
    private SurveyService surveyService;

    @GetMapping(value = "/{addressId}")
    public ResponseEntity<List<SurveyEvent>> findAllSurveyByAddress(@PathVariable("addressId") BigInteger addressId) {
        if (addressId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        BigInteger userId = AuthorizedUser.id();
        List<SurveyEvent> surveyEventList = surveyService.findAllSurveyEventByAddress(addressId, userId);
        if (surveyEventList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(surveyEventList, HttpStatus.OK);
    }

    @GetMapping(value = "/single/{surveyId}")
    public ResponseEntity<SurveyEvent> findSurveyByAddress(@PathVariable("surveyId") BigInteger surveyId) {
        BigInteger userId = AuthorizedUser.id();
        SurveyEvent surveyEvent = surveyService.findOne(surveyId);
        if (surveyEvent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(surveyEvent, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SurveyEvent> addSurveyEvent(@RequestBody SingleSurveyEvent singleSurveyEvent) {
        BigInteger userId = AuthorizedUser.id();
        if (singleSurveyEvent == null || singleSurveyEvent.getAddressId() == null) return new ResponseEntity<>(HttpStatus.CONFLICT);
        SurveyEvent surveyEvent = surveyService.addSurveyEvent(singleSurveyEvent, userId);
        if (surveyEvent == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(surveyEvent, HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SurveyEvent> addVoiceToSurvey(@RequestBody SingleVote singleVote) {
        BigInteger userId = AuthorizedUser.id();
        if (singleVote == null || singleVote.getSurveyId() == null || singleVote.getAddressId() == null) return new ResponseEntity<>(HttpStatus.CONFLICT);
        SurveyEvent surveyEvent = surveyService.addVoiceToSurvey(singleVote, userId);
        if (surveyEvent == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(surveyEvent, HttpStatus.OK);
    }

    @PutMapping(value = "/close", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SurveyEvent> finishSurvey(@RequestBody SingleSurveyEventById singleSurveyEventById) {
        BigInteger userId = AuthorizedUser.id();
        SurveyEvent surveyEvent = surveyService.finishSurvey(singleSurveyEventById.getSurveyId(), userId);
        if (surveyEvent == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(surveyEvent, HttpStatus.OK);
    }
}
