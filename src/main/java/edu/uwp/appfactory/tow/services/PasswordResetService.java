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

@Service
public class PasswordResetService {

    private final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

    private final JavaMailSender javaMailSender;

    private final ContentBuilder contentBuilder;

    @Autowired
    public PasswordResetService(JavaMailSender javaMailSender, ContentBuilder contentBuilder) {
        this.javaMailSender = javaMailSender;
        this.contentBuilder = contentBuilder;
    }

    public boolean sendResetMail(Users user, int token) {
        try {
                String userName = "Hi, " + user.getFirstname() + " " + user.getLastname();

                String message = contentBuilder.buildEmail(userName, token);
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
}
