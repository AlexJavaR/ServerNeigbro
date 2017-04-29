package com.prototype.model.event;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Event")
public abstract class GlobalEvent extends Event
{

    public GlobalEvent(LocalDateTime dateEvent) {
        super(dateEvent);
    }
}
