package com.devsmart.mailbannana;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SMTPConnectionTest {

    @Test
    public void testRunCommand() throws Exception {

        SMTPServer server = mock(SMTPServer.class);
        Socket socket = mock(Socket.class);

        InputStream subInputStream = IOUtils.toInputStream(
        "HELO test.example.com\r\n" +
        "MAIL FROM:<paul@example.com>\r\n" +
        "RCPT TO:<bob@nasa.gov>\r\n" +
        "DATA\r\n" +
        "From: \"Paul S.\" <paul@example.com>\r\n" +
        "To: \"Bob dude\" <bob@nasa.gov>\r\n" +
        "Date: Tue, 15 January 2008 16:02:43 -0500\r\n" +
        "Subject: Test Message\r\n" +
        "\r\n" +
        "Hello Bob, This is a test message\r\n" +
        ".\r\n" +
        "QUIT\r\n"
        );

        OutputStream subOutputStream = new ByteArrayOutputStream();

        when(socket.getInputStream()).thenReturn(subInputStream);
        when(socket.getOutputStream()).thenReturn(subOutputStream);

        SMTPConnection connection = new SMTPConnection(socket, server);
        connection.run();

        String recived = subOutputStream.toString();
        System.out.println(recived);
    }
}
