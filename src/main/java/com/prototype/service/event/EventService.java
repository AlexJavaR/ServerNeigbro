package com.prototype.service.event;

import com.prototype.model.event.Event;

import java.math.BigInteger;
import java.util.List;

public interface EventService {
    List<Event> findGeneralEventsAsHousemate(BigInteger addressId, String apartment, BigInteger userId);

    List<Event> findGeneralEventsAsManager(BigInteger addressId, BigInteger userId);
}
