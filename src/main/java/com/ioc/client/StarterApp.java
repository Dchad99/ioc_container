package com.ioc.client;

public class StarterApp {
    public static void main(String[] args) {
        DefaultEmailSender emailSender = new DefaultEmailSender();
        emailSender.setPort(1234);
        emailSender.setProtocol("FTTPSSS");

        UserService service = new UserService();
        service.setEmailSender(emailSender);
        service.sendUpdates();

    }
}
