package com.prototype.service.exception;

import com.prototype.util.exception.Developers;
import com.prototype.util.exception.Device;
import com.prototype.util.exception.ExceptionReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ExceptionReportSenderImpl implements ExceptionReportSender {

    private static final String FROM = "neigh@bro.com";         // this wouldn`t work (Google security)
    private static final String EMAIL_SUBJECT = "Ошибка в NeighBro на ";
    private Map<Device, Set<Developers>> developers;
    private JavaMailSender mailSender;

    @Autowired
    public ExceptionReportSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        developers = new ConcurrentHashMap<>();
        developers.put(Device.WEB, new HashSet<>(Arrays.asList(Developers.ANDREI, Developers.MARI)));
        developers.put(Device.ANDROID, new HashSet<>(Arrays.asList(Developers.ALEX_MELESHKO, Developers.VLAD_PAPAVA)));
        developers.put(Device.IOS, new HashSet<>(Arrays.asList(Developers.VLAD_DORFMAN)));
        developers.put(Device.SERVER, new HashSet<>(Arrays.asList(Developers.ALEX_MELESHKO, Developers.ANDREI)));
    }

    private String[] getRecipients(Device device){
        List<String> listEmails = developers.get(device)
                .stream()
                .map(d -> String.valueOf(d.email()))
                .collect(Collectors.toList());
        String[] arrayEmails = new String[listEmails.size()];
        return listEmails.toArray(arrayEmails);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendExceptionReport(ExceptionReport report) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(FROM);
            messageHelper.setTo(getRecipients(report.getDevice()));
            messageHelper.setSubject(EMAIL_SUBJECT + report.getDevice());
            String textUserId = report.getUserId() == null ? "" : "\n\nUser id:" + String.valueOf(report.getUserId());
            messageHelper.setText(
                    "Hey bro! There`s an error on " + report.getDevice() +
                    "\n\nClient details:\n" + report.getUseragent() +
                     textUserId +
                    "\n\nTry to fix it, catch the stacktrace:\n" + report.getStacktrace());
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}
