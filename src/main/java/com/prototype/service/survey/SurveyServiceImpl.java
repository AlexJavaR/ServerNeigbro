package com.prototype.service.survey;

import com.prototype.model.Address;
import com.prototype.model.User;
import com.prototype.model.event.survey.SurveyEvent;
import com.prototype.repository.address.AddressRepository;
import com.prototype.repository.event.EventRepository;
import com.prototype.repository.user.UserRepository;
import com.prototype.to.survey.SingleSurveyEvent;
import com.prototype.to.survey.SingleVote;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@Service("SurveyService")
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<SurveyEvent> findAllSurveyEventByAddress(BigInteger addressId, BigInteger userId) {
        ObjectId objectAddressId;
        try {
            objectAddressId = new ObjectId(addressId.toString(16));
        } catch (IllegalArgumentException e) {
            return null;
        }
        return eventRepository.findAllSurveyEventByAddress(objectAddressId);
    }

    @Override
    public SurveyEvent addSurveyEvent(SingleSurveyEvent singleSurveyEvent, BigInteger userId) {
        Address address = addressRepository.findOne(singleSurveyEvent.getAddressId());
        User author = userRepository.findOne(userId);
        if (address == null) return null;
        SurveyEvent surveyEvent = new SurveyEvent(singleSurveyEvent.getTitleSurvey(), LocalDateTime.now(), address, author,
                singleSurveyEvent.getDescriptionSurvey(), singleSurveyEvent.isSingleAnswer());
        for (String answer : singleSurveyEvent.getAnswers()) {
            surveyEvent.getAnswers().put(answer, new ArrayList<>());
        }
        return eventRepository.save(surveyEvent);
    }

    @Override
    public SurveyEvent addVoiceToSurvey(SingleVote singleVote, BigInteger userId) {
        SurveyEvent currentSurveyEvent = (SurveyEvent) eventRepository.findOne(singleVote.getSurveyId());
        User user = userRepository.findOne(userId);
        Address address = addressRepository.findOne(singleVote.getAddressId());

        if (userRepository.getAddressDataByAddress(user, address) == null) return null;
        Map<String, ArrayList<User>> answers = currentSurveyEvent.getAnswers();
        for (Map.Entry<String, ArrayList<User>> answer : answers.entrySet()) {
            if (answer.getValue().contains(user)) return null;
        }
        for (String answer : singleVote.getAnswers()) {
            if (answers.containsKey(answer)) {
                answers.get(answer).add(user);
            }
        }
        return eventRepository.save(currentSurveyEvent);
    }

    @Override
    public SurveyEvent findOne(BigInteger surveyId) {
        return (SurveyEvent) eventRepository.findOne(surveyId);
    }

    @Override
    public SurveyEvent finishSurvey(BigInteger surveyId, BigInteger userId) {
        SurveyEvent surveyEvent = (SurveyEvent) eventRepository.findOne(surveyId);
        if (surveyEvent.getAuthor().getId().equals(userId)) {
            surveyEvent.setEnabled(false);
            return eventRepository.save(surveyEvent);
        } else {
            return null;
        }
    }
}
