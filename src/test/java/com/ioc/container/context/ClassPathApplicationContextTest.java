package com.ioc.container.context;

import com.ioc.client.DefaultEmailSender;
import com.ioc.client.EmailSender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClassPathApplicationContextTest {
    private static DefaultEmailSender expected;
    private ApplicationContext context = new ClassPathApplicationContext("context.xml");

    @BeforeAll
    static void init(){
        expected = new DefaultEmailSender();
        expected.setPort(1234);
        expected.setProtocol("TCP");
    }

    @Test
    @DisplayName("Test context method - getBean(String id)")
    void testGetById(){
        EmailSender actual = (EmailSender) context.getBean("emailService");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test context - getBean(Class className)")
    void testGetBeanByClassName(){
        DefaultEmailSender actual = context.getBean(DefaultEmailSender.class);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test context - getBean(String id, Class className)")
    void testGetByBeanIdAndClassName() {
        DefaultEmailSender actual = context.getBean("emailService", DefaultEmailSender.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}