package com.ioc.container;

import com.ioc.client.DefaultEmailSender;
import com.ioc.client.EmailSender;
import com.ioc.client.UserService;
import com.ioc.container.context.ApplicationContext;
import com.ioc.container.context.ClassPathApplicationContext;

public class Starter {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathApplicationContext("applicationContext.xml");

        DefaultEmailSender emailSender = (DefaultEmailSender) applicationContext.getBean("emailService");

        UserService service = (UserService) applicationContext.getBean("userService");
        service.setEmailSender(emailSender);
        System.out.println(service);
    }
}
