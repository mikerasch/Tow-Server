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
 * The async email class is responsible for sending the initial verification email and the password reset emails.
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

    /**
     * The sendEmailAsync method generates an email using the user info and the current dns config info
     * that will be used to generate the bottom the user receive in the email to verify.
     * @param user the user that is attempting to sign up for the tow service
     */
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

    /**
     * Nearly identical to the sendemailasync method above but needs the users jwt token to
     * know which account to begin the reset process with.
     * @param user
     * @param token
     */
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
