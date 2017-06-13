package com.prototype.model.event.survey;

import com.prototype.model.Address;
import com.prototype.model.User;
import com.prototype.model.event.UserEvent;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "Event")
public class SurveyEvent extends UserEvent {
    private Map<String, ArrayList<User>> answers;
    private boolean enabled = true;
    private boolean singleAnswer;

    public SurveyEvent(String titleEvent, LocalDateTime dateEvent, Address address, User author, String description, boolean singleAnswer) {
        super(titleEvent, dateEvent, address, author, description);
        this.answers = new HashMap<>();
        this.singleAnswer = singleAnswer;
    }

    public Map<String, ArrayList<User>> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, ArrayList<User>> answers) {
        this.answers = answers;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSingleAnswer() {
        return singleAnswer;
    }

    public void setSingleAnswer(boolean singleAnswer) {
        this.singleAnswer = singleAnswer;
    }
}
