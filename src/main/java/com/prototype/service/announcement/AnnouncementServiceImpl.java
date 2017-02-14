package com.prototype.service.announcement;

import com.prototype.model.Address;
import com.prototype.model.User;
import com.prototype.model.event.announcement.AnnouncementEvent;
import com.prototype.model.event.announcement.UserAnnouncementEvent;
import com.prototype.repository.address.AddressRepository;
import com.prototype.repository.event.EventRepository;
import com.prototype.repository.user.UserRepository;
import com.prototype.to.UserAnnouncement;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service("AnnouncementService")
public class AnnouncementServiceImpl implements AnnouncementService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<UserAnnouncementEvent> findAllAnnouncementsOfAddress(BigInteger addressId) {
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
        return eventRepository.findAllAnnouncementsOfAddress(objectAddressId);
    }

    @Override
    public UserAnnouncementEvent addAnnouncement(UserAnnouncement announcement, BigInteger userId) {
        User user = userRepository.findOne(userId);
        Address address = addressRepository.findOne(announcement.getAddressId());
        UserAnnouncementEvent userAnnouncementEvent = new UserAnnouncementEvent(announcement.getTitleAnnouncement(), LocalDateTime.now(), address, user, announcement.getTextAnnouncement());
        return eventRepository.save(userAnnouncementEvent);
    }

    @Override
    public AnnouncementEvent findOne(BigInteger id) {
        return (AnnouncementEvent) eventRepository.findOne(id);
    }
}
