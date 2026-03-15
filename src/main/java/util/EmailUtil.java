package util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

    private static final String FROM_EMAIL = "nguyentailoi.ce191482@gmail.com";
    private static final String PASSWORD = "tvyaqxhkwfdhvdxg";

    public static void sendOTP(String toEmail, String otp) throws Exception {

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(FROM_EMAIL));

        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(toEmail)
        );

        message.setSubject("Clinic System Password Reset");

        message.setText("Your OTP code is: " + otp);

        Transport.send(message);
    }
}