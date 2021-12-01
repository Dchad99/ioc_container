package com.ioc.client;

import lombok.Data;

@Data
public class DefaultEmailSender implements EmailSender{
    private String protocol;
    private int port;

    public void sendEmail(String email) {
        System.out.println("Send email" + email);
    }
}
