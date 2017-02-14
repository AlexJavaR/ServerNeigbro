package com.prototype.to;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserMessage {
    private String message;

    public UserMessage(@JsonProperty("message") String message )
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
