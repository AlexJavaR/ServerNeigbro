package com.prototype.to.survey;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class SingleSurveyEventById {
    private BigInteger surveyId;

    public SingleSurveyEventById(@JsonProperty("surveyId") BigInteger surveyId) {
        this.surveyId = surveyId;
    }

    public BigInteger getSurveyId() {
        return surveyId;
    }
}
