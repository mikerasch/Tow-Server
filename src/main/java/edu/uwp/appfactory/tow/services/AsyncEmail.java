package edu.uwp.appfactory.tow.services;

import edu.uwp.appfactory.tow.entities.FailedEmail;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.repositories.FailedEmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class AsyncEmail {

    private final JavaMailSender javaMailSender;
    private final ContentBuilder contentBuilder;
    private final FailedEmailRepository failedEmailRepository;
    private final Logger logger = LoggerFactory.getLogger(AsyncEmail.class);
    @Value("${SPRING_DNS}")
    private String dns;

    @Autowired
    public AsyncEmail(JavaMailSender javaMailSender, ContentBuilder contentBuilder, FailedEmailRepository failedEmailRepository) {
        this.javaMailSender = javaMailSender;
        this.contentBuilder = contentBuilder;
        this.failedEmailRepository = failedEmailRepository;
    }

    @Async
    public void sendEmailAsync(Users user) {
        try {
            String userName = "Hi, " + user.getFirstname() + " " + user.getLastname();
            String verifyLink = dns + "api/auth/verification?token=" + user.getVerifyToken();
            String message = contentBuilder.buildVerifyEmail(userName, verifyLink);

            MimeMessagePreparator messagePreparation = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom("DoNotReply", "DoNotReply");
                messageHelper.setTo(user.getEmail());
                messageHelper.setSubject("Email Verification");
                messageHelper.setText(message, true);
            };

            javaMailSender.send(messagePreparation);

        } catch (MailException e) {
            logger.error(e.getMessage());
            FailedEmail failedEmail = FailedEmail.builder()
                    .email(user.getEmail())
                    .user_uuid(user.getId())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .verify_token(user.getVerifyToken())
                    .build();
            failedEmailRepository.save(failedEmail);
        }
    }

    @Async
    public void sendResetEmailAsync(Users user, int token) {
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

        } catch (MailException e) {
            logger.error(e.getMessage());
        }
    }
}
