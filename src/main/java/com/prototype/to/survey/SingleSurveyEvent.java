package com.prototype.to.survey;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.List;

public class SingleSurveyEvent {
    private BigInteger addressId;
    private String titleSurvey;
    private String descriptionSurvey;
    private boolean singleAnswer;
    private List<String> answers;

    public SingleSurveyEvent(@JsonProperty("addressId") BigInteger addressId,
                             @JsonProperty("titleSurvey") String titleSurvey,
                             @JsonProperty("descriptionSurvey") String descriptionSurvey,
                             @JsonProperty("singleAnswer") boolean singleAnswer,
                             @JsonProperty("answers") List<String> answers) {
        this.addressId = addressId;
        this.titleSurvey = titleSurvey;
        this.descriptionSurvey = descriptionSurvey;
        this.singleAnswer = singleAnswer;
        this.answers = answers;
    }

    public BigInteger getAddressId() {
        return addressId;
    }

    public String getTitleSurvey() {
        return titleSurvey;
    }

    public String getDescriptionSurvey() {
        return descriptionSurvey;
    }

    public boolean isSingleAnswer() {
        return singleAnswer;
    }

    public List<String> getAnswers() {
        return answers;
    }
}
