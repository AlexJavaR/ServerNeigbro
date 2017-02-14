package com.prototype.service.event;

import com.prototype.model.event.Event;
import com.prototype.repository.event.EventRepository;
import com.prototype.repository.user.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service("EventService")
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Event> findGeneralEventsOfAddress(BigInteger addressId, BigInteger userId) {
        String apartment = userRepository.findOne(userId).getApartment(addressId);
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
        return eventRepository.findGeneralEventsOfAddress(objectAddressId, apartment);
    }
}
