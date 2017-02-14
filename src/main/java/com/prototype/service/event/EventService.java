package com.prototype.service.event;

import com.prototype.model.event.Event;

import java.math.BigInteger;
import java.util.List;

public interface EventService {
    List<Event> findGeneralEventsOfAddress(BigInteger addressId, BigInteger userId);
}
