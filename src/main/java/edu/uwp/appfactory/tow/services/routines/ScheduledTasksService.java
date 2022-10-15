package edu.uwp.appfactory.tow.services.routines;

import edu.uwp.appfactory.tow.entities.FailedEmail;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.repositories.FailedEmailRepository;
import edu.uwp.appfactory.tow.services.email.ContentBuilderService;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * The chron services that will determine when a user needs to be reminded that they havent finished teh registration process.
 */
@Service
public class ScheduledTasksService {
    private final FailedEmailRepository failedEmailRepository;
    private final UsersRepository usersRepository;
    private final ContentBuilderService contentBuilderService;
    private final JavaMailSender javaMailSender;
    private final Logger logger = LoggerFactory.getLogger(ScheduledTasksService.class);
    private static final String DONOTREPLY = "DoNotReply";
    @Value("${SPRING_DNS}")
    private String dns;

    public ScheduledTasksService(FailedEmailRepository failedEmailRepository, UsersRepository usersRepository, ContentBuilderService contentBuilderService, JavaMailSender javaMailSender) {
        this.failedEmailRepository = failedEmailRepository;
        this.usersRepository = usersRepository;
        this.contentBuilderService = contentBuilderService;
        this.javaMailSender = javaMailSender;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkFailedEmail() {
        Iterable<FailedEmail> failedEmails = failedEmailRepository.findAll();
        failedEmails.forEach(entity -> {
            try {
                String userName = "Hi, " + entity.getFirstname() + " " + entity.getLastname();
                String verifyLink = dns + "api/users/verification?token=" + entity.getVerify_token();

                String message = contentBuilderService.buildVerifyEmail(userName, verifyLink);

                MimeMessagePreparator messagePreparation = mimeMessage -> {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                    messageHelper.setFrom(DONOTREPLY, DONOTREPLY);
                    messageHelper.setTo(entity.getEmail());
                    messageHelper.setSubject("Email Verification");
                    messageHelper.setText(message, true);
                };

                javaMailSender.send(messagePreparation);

                failedEmailRepository.deleteById(entity.getUuid());
            } catch (MailException e) {
                logger.error(e.getMessage());
            }
        });
    }

    @Scheduled(cron = "0 0 1 * * *")
    private void checkVerifyStatus() {
        List<Users> nonVerifiedUsers = usersRepository.findAllNonVerified();
        if (nonVerifiedUsers.isEmpty()) {
            return;
        }
        nonVerifiedUsers.forEach(entity -> {
            LocalDate userVerifyDate = LocalDate.parse(entity.getVerifyDate());
            Period periodBetween = Period.between(userVerifyDate, LocalDate.now());
            if (periodBetween.getDays() > 8) {
                usersRepository.deleteByEmail(entity.getEmail());
            } else if (periodBetween.getDays() == 4) {
                try {
                    String userName = "Hi, " + entity.getFirstname() + " " + entity.getLastname();
                    String verifyLink = dns + "api/users/verification?token=" + entity.getVerifyToken();

                    String message = contentBuilderService.buildReminderEmail(userName, verifyLink);

                    MimeMessagePreparator messagePreparation = mimeMessage -> {
                        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                        messageHelper.setFrom(DONOTREPLY, DONOTREPLY);
                        messageHelper.setTo(entity.getEmail());
                        messageHelper.setSubject("Email Verification Reminder");
                        messageHelper.setText(message, true);
                    };
                    javaMailSender.send(messagePreparation);
                } catch (MailException e) {
                    logger.error(e.getMessage());
                }
            }
        });
    }
}
