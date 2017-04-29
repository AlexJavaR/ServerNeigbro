package com.prototype.service.event;

import com.prototype.model.event.Event;
import com.prototype.repository.event.EventRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service("EventService")
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Event> findGeneralEventsAsHousemate(BigInteger addressId, String apartment, BigInteger userId) {
        //String apartment = userRepository.findOne(userId).getApartment(addressId);
        ObjectId objectAddressId;
        try {
            objectAddressId = new ObjectId(addressId.toString(16));
        } catch (IllegalArgumentException e) {
            return null;
        }
        return eventRepository.findGeneralEventsAsHousemate(objectAddressId, apartment);
    }

    @Override
    public List<Event> findGeneralEventsAsManager(BigInteger addressId, BigInteger userId) {
        ObjectId objectAddressId;
        try {
            objectAddressId = new ObjectId(addressId.toString(16));
        } catch (IllegalArgumentException e) {
            return null;
        }
        return eventRepository.findGeneralEventsAsManager(objectAddressId);
    }
}
