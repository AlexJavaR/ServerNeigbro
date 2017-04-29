package com.prototype.service.email;


import com.prototype.model.Address;
import com.prototype.model.User;

public interface EmailService {

    void sendNoticeAboutRequestForWithdrawalMoney(User user, Address address, Integer amount);

    void sendFeedback(String name, String email, String phone, String message);
}
