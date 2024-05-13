package com.example.spebackend.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private OtpUtil otpUtil;


    public String sendOtpEmail(String email) {
        String otp = otpUtil.generateOtp();
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true, "UTF8");

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("LaayaXXX Music OTP:");

            String htmlContent = String.format(
                "Your One Time Password (OTP) for LaayaXXX account is:" +
                "%s" +
                "Regards," +
                "Team LaayaXXX Music"
                , otp);
            mimeMessageHelper.setText(htmlContent);
            javaMailSender.send(mimeMailMessage);
        } catch (MessagingException e) {

            // Notify the user about the error
            String errorMessage = "An error occurred while sending the email. Please try again later.";
            // You can use your application's error handling mechanism to notify the user, such as showing a message on the UI or sending a notification

            // Alternatively, you can rethrow the exception to propagate it further
            throw new RuntimeException(errorMessage, e);
        }
        return otp;
    }
}
