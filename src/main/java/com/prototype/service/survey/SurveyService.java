package com.prototype.service.survey;

import com.prototype.model.event.survey.SurveyEvent;
import com.prototype.to.survey.SingleSurveyEvent;
import com.prototype.to.survey.SingleVote;

import java.math.BigInteger;
import java.util.List;

public interface SurveyService {
    List<SurveyEvent> findAllSurveyEventByAddress(BigInteger addressId, BigInteger userId);

    SurveyEvent addSurveyEvent(SingleSurveyEvent singleSurveyEvent, BigInteger userId);

    SurveyEvent addVoiceToSurvey(SingleVote singleVote, BigInteger userId);

    SurveyEvent findOne(BigInteger surveyId);

    SurveyEvent finishSurvey(BigInteger surveyId, BigInteger userId);
}
