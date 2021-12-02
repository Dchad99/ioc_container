package com.ioc.client;

import lombok.Data;

import java.util.List;

@Data
public class UserService {
    private String name;
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
