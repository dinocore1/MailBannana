package com.devsmart.mailbannana;


import org.junit.Test;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmailTest {


    @Test
    public void sendEmail() throws Exception {

        Properties props = new Properties();
        props.put("mail.smtp.host", "gmail-smtp-in.l.google.com");

        Session session = Session.getDefaultInstance(props);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("rad@example.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("someplace@gmail.com"));
        message.setSubject("This is a test");
        message.setText("Hello this is a message");

        Transport.send(message);


    }
}
