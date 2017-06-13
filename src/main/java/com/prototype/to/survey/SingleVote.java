package com.prototype.to.survey;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SingleVote {
    private BigInteger addressId;
    private BigInteger surveyId;
    private List<String> answers;

    public SingleVote(@JsonProperty("addressId") BigInteger addressId, @JsonProperty("surveyId") BigInteger surveyId, @JsonProperty("answers") List<String> answers) {
        this.addressId = addressId;
        this.surveyId = surveyId;
        this.answers = answers;
    }

    public BigInteger getAddressId() {
        return addressId;
    }

    public BigInteger getSurveyId() {
        return surveyId;
    }

    public List<String> getAnswers() {
        return answers;
    }
}
