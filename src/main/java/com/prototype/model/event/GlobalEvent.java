package com.prototype.model.event;

import com.prototype.model.User;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Event")
public abstract class GlobalEvent extends Event
{
    private User author;

    public GlobalEvent(LocalDateTime dateEvent, User author) {
        super(dateEvent);
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }
}
