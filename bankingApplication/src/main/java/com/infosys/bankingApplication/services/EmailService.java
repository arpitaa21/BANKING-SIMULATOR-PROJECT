package com.infosys.bankingApplication.services;

import com.infosys.bankingApplication.configs.MailConfig;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final MailConfig mailConfig;

    public EmailService(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    public void sendLowBalanceAlert(String toEmail, String name, double balance) {
        sendEmail(toEmail, "⚠ Low Balance Alert",
                "Hello " + name + ",\n\n" +
                "Your account balance is LOW.\n\n" +
                "Current Balance: ₹" + balance + "\n\n" +
                "Please deposit funds.\n\n— Banking System");
    }

    public void sendMaxBalanceAlert(String toEmail, String name, double balance) {
        sendEmail(toEmail, "⚠ Maximum Balance Alert",
                "Hello " + name + ",\n\n" +
                "Your account balance exceeded maximum limit.\n\n" +
                "Current Balance: ₹" + balance + "\n\n— Banking System");
    }

    private void sendEmail(String toEmail, String subject, String text) {
        Session session = Session.getInstance(mailConfig.getProperties(),
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailConfig.getUsername(), mailConfig.getPassword());
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailConfig.getUsername()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
