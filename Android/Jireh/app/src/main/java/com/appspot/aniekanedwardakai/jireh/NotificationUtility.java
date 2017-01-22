package com.appspot.aniekanedwardakai.jireh;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.Properties;


/**
 * Created by Teddy on 11/12/2016.
 */

public class NotificationUtility {

    private static void sendEmail(final ArrayList<String> to, final String subject, final String content) throws EmailException {
//        Email email = new SimpleEmail();
//        email.setHostName("smtp.gmail.com");
//        email.setSmtpPort(465);

//        email.setAuthenticator(new DefaultAuthenticator("aniekan.jireh@gmail.com", "jireh91!"));
//        email.setSSLOnConnect(true);
//        email.setFrom("aniekan.jireh@gmail.com");
//        email.setSubject(subject);
//        email.setMsg(content);
//        for(int i=0; i<to.size(); i++){
//            email.addTo(to.get(i));
//        }
//        email.send();

            Runnable sendMessage = new Runnable() {
                @Override
                public void run() {
                    try {
                        final String username = "aniekan.jireh@gmail.com";
                        final String password = "jireh91!";

                        Properties props = new Properties();
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.starttls.enable", "true");
                        props.put("mail.smtp.host", "smtp.gmail.com");
                        props.put("mail.smtp.port", "587");

                        Session session = Session.getInstance(props,
                                new javax.mail.Authenticator() {
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication(username, password);
                                    }
                                });
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(username));
                        for(int i=0; i<to.size(); i++){
//                            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to.get(i)));
                            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to.get(i)));
                        }
                        message.setSubject(subject);
                        message.setText(content);
                        Transport.send(message);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            new Thread(sendMessage).start();

            System.out.println("Done");
    }


    public static void sendToAllAdmins(String subject, String content) throws EmailException {

        ArrayList<String> toList = new ArrayList<String>();

        for(int i=0; i<TempDB.adminUsers.size(); i++){
            toList.add(TempDB.adminUsers.get(i).getEmail());
        }

        sendEmail(toList, subject, content);
    }
}
