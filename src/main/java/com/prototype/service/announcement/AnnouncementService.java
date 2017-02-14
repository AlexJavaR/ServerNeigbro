package com.prototype.service.announcement;

import com.prototype.model.event.announcement.AnnouncementEvent;
import com.prototype.model.event.announcement.UserAnnouncementEvent;
import com.prototype.to.UserAnnouncement;

import java.math.BigInteger;
import java.util.List;

public interface AnnouncementService {
    List<UserAnnouncementEvent> findAllAnnouncementsOfAddress(BigInteger addressId);

    UserAnnouncementEvent addAnnouncement(UserAnnouncement announcement, BigInteger userId);

    AnnouncementEvent findOne(BigInteger id);
}
