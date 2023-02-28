package edu.uwp.appfactory.tow.controllers.email;

import edu.uwp.appfactory.tow.entities.FailedEmail;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.repositories.FailedEmailRepository;
import edu.uwp.appfactory.tow.requestobjects.email.SupportEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Responsible for sending the initial verification email and the password reset email.
 */
@Service
public class AsyncEmailService {

    private final JavaMailSender javaMailSender;
    private final ContentBuilderService contentBuilderService;
    private final FailedEmailRepository failedEmailRepository;
    private final Logger logger = LoggerFactory.getLogger(AsyncEmailService.class);
    private static final String DONOTREPLY = "DoNotReply";
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    @Value("${SPRING_DNS}")
    private String dns;

    @Value("${CUSTOMER_SUPPORT_EMAIL}")
    private String customerSupportEmail;

    public AsyncEmailService(JavaMailSender javaMailSender, ContentBuilderService contentBuilderService, FailedEmailRepository failedEmailRepository) {
        this.javaMailSender = javaMailSender;
        this.contentBuilderService = contentBuilderService;
        this.failedEmailRepository = failedEmailRepository;
    }

    public void sendCustomerSupportEmail(SupportEmail supportEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
          MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
          messageHelper.setTo(supportEmail.getSenderEmail());
          messageHelper.setSubject(supportEmail.getSubject());
          messageHelper.setText(supportEmail.getMessage());
          messageHelper.setCc(customerSupportEmail);
        };
        javaMailSender.send(messagePreparator);
    }

    /**
     * The sendEmailAsync method generates an email using the user info and the current dns config info
     * that will be used to generate the bottom the user receive in the email to verify.
     * @param user the user that is attempting to sign up for the tow service
     */
    public void sendSignupEmail(Users user) {
        try {
            String userName = "Hi, " + user.getFirstname() + " " + user.getLastname();
            String verifyLink = dns + "api/auth/verification?token=" + user.getVerifyToken();
            String message = contentBuilderService.buildVerifyEmail(userName, verifyLink);

            MimeMessagePreparator messagePreparation = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom(DONOTREPLY, DONOTREPLY);
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
     * @param user user that is requesting a reset
     * @param token token of the user for authentication purposes
     */

    public void sendResetEmailAsync(Users user, int token) {
        try {
            String userName = "Hi, " + user.getFirstname() + " " + user.getLastname();
            String message = contentBuilderService.buildPasswordEmail(userName, token);

            MimeMessagePreparator messagePreparation = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom(DONOTREPLY, DONOTREPLY);
                messageHelper.setTo(user.getEmail());
                messageHelper.setSubject("Password Reset");
                messageHelper.setText(message, true);
            };

            javaMailSender.send(messagePreparation);

        } catch (MailException e) {
            logger.error(e.getMessage());
        }
    }
    public void submitSignupEmailExecution(Users user){
        Runnable runnable = () -> sendSignupEmail(user);
        executor.execute(runnable);
    }
    public void submitCustomerServiceEmail(SupportEmail supportEmail) {
        Runnable runnable = () -> sendCustomerSupportEmail(supportEmail);
        executor.execute(runnable);
    }
    public void submitEmailResetExecution(Users user, int token){
        Runnable runnable = () -> sendResetEmailAsync(user,token);
        executor.execute(runnable);
    }
}