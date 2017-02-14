package com.prototype.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prototype.model.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Event")
public abstract class Event extends BaseEntity
{
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime dateEvent;

    public Event(LocalDateTime dateEvent) {
        this.dateEvent = dateEvent;
    }

    public LocalDateTime getDateEvent() {
        return dateEvent;
    }
}
