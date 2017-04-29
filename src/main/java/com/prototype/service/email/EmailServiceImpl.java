package com.prototype.service.email;

import com.prototype.model.Address;
import com.prototype.model.BankAccount;
import com.prototype.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailService;

    @Override
    public void sendNoticeAboutRequestForWithdrawalMoney(User user, Address address, Integer amount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        BankAccount bankAccount = user.getBankAccount();
        double doubleAmount = ((double) amount) / 100;
        double accountBalance = ((double) address.getAccountBalance()) / 100;
        String[] to = {"denis@brovarnyy.com", "alemeleshko@gmail.com"};
        mailMessage.setTo(to);
        mailMessage.setSubject("Withdrawal money");
        mailMessage.setText("Hello, " + user.getEmail() + " want your money!!!" +
                "\n\nName: " + bankAccount.getFirstName() + " " + bankAccount.getLastName() +
                "\nBank: " + bankAccount.getBank() +
                "\nBranch: " + bankAccount.getBranch() +
                "\nAccount: " + bankAccount.getAccount() +
                "\nComment: " + bankAccount.getComment() +
                "\n\nAddress: " + address.getGoogleAddress().getDescription() +
                "\nAddressID: " + address.getId() +
                "\nCurrent account balance: " + accountBalance +
                "\nAmount to withdrawal: " + doubleAmount + "₪" +
                "\nManager phone number: " + address.getPhoneNumber());
        javaMailService.send(mailMessage);
    }

    @Override
    public void sendFeedback(String name, String email, String phone, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String[] to = {"denis@brovarnyy.com", "alemeleshko@gmail.com", "ahaytukam@gmail.com"};
        mailMessage.setTo(to);
        mailMessage.setSubject("Feedback from NeighBro");
        mailMessage.setText("Hello, this mail came from feedback form" +
                "\n\nName: " + name +
                "\nEmail: " + email +
                "\nPhone: " + phone +
                "\nMessage: " + message);
        javaMailService.send(mailMessage);
    }
}
