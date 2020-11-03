package edu.uwp.appfactory.tow.services;

import edu.uwp.appfactory.tow.entities.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class EmailService {

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;

    private final ContentBuilder contentBuilder;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, ContentBuilder contentBuilder) {
        this.javaMailSender = javaMailSender;
        this.contentBuilder = contentBuilder;
    }

    public boolean sendResetMail(Users user, int token) {
        try {
            String userName = "Hi, " + user.getFirstname() + " " + user.getLastname();

            String message = contentBuilder.buildPasswordEmail(userName, token);
            MimeMessagePreparator messagePreparation = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom("DoNotReply", "DoNotReply");
                messageHelper.setTo(user.getEmail());
                messageHelper.setSubject("Password Reset");
                messageHelper.setText(message, true);
            };
            javaMailSender.send(messagePreparation);

            return true;
        } catch (MailException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public boolean sendVerifyMail(Users user, String verifyToken) {
        try {
            String userName = "Hi, " + user.getFirstname() + " " + user.getLastname();
            String verifyLink = "https://help-spring-api-2.herokuapp.com/api/users/verification?token=" + verifyToken;

            String message = contentBuilder.buildVerifyEmail(userName, verifyLink);
            MimeMessagePreparator messagePreparation = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom("DoNotReply", "DoNotReply");
                messageHelper.setTo(user.getEmail());
                messageHelper.setSubject("Email Verification");
                messageHelper.setText(message, true);
            };
            javaMailSender.send(messagePreparation);

            return true;
        } catch (MailException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
