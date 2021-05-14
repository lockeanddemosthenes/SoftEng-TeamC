package edu.wpi.teamC;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    private Message message;
    private Session session;
    public SendEmail(String recipient, String sender){
        String to = recipient;

        final String username = sender;
        final String password = "SoftEng2021";

        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            message = new MimeMessage(session);

            message.setFrom(new InternetAddress(sender));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void setMessageSubject(String subject) throws MessagingException {
        message.setSubject(subject);
    }

    public void setMessageText(String text) throws MessagingException {
        message.setText(text);
    }

    public void sendMessage() throws MessagingException {
        Transport.send(message);
    }
}
