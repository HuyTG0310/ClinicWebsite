
package util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

    private static final String FROM_EMAIL = "clinic.website.demo@gmail.com";
    private static final String PASSWORD = "lhjz ftpl fwxs siiu";

    private static Session getSession() {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props,
                new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });
    }

    // Send OTP for password reset
    public static void sendOTP(String toEmail, String otp) throws Exception {

        Session session = getSession();

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

    // Send account info for staff
    public static void sendAccount(String toEmail, String username) {

        try {

            Session session = getSession();

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(FROM_EMAIL));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );

            message.setSubject("Clinic Staff Account");

            message.setText(
                    "Your account has been created\n\n"
                    + "Username: " + username + "\n"
                    + "Password: 123\n\n"
                    + "Please change password after login."
            );

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}