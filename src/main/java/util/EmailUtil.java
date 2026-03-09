/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
/**
 *
 * @author ADMIN
 */
public class EmailUtil {
    public static void sendAccount(String toEmail, String username) {

        final String fromEmail = "clinic.website.demo@gmail.com";
        final String appPassword = "lhjz ftpl fwxs siiu";
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(fromEmail));

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
