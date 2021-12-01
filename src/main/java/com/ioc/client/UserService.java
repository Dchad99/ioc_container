package com.ioc.client;

import java.util.List;

public class UserService {
    private EmailSender emailSender;

    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendUpdates() {
        List<String> users = List.of("David", "Roman");
        for (String user : users) {
            emailSender.sendEmail(user);
        }
    }
}
