package edu.uwp.appfactory.tow.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * This class is responsible for constructing the emails contents that asyncemail will send to the user.
 */
@Service
public class ContentBuilderService {
    private final TemplateEngine templateEngine;

    @Autowired
    public ContentBuilderService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildPasswordEmail(String userName, int token) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("token", token);
        return templateEngine.process("mailTemplatePassword", context);
    }

    public String buildVerifyEmail(String userName, String verifyLink) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("verifyLink", verifyLink);
        return templateEngine.process("mailTemplateVerify", context);
    }


    /**
     * this method constructs the email used by a chron job to send a user a reminder
     * email who hasnt yet completed the initial verification process.
     * @param userName name of the user that needs reminding
     * @param verifyLink generated link that will route a user to the password reset page
     * @return returns the generated email
     */
    public String buildReminderEmail(String userName, String verifyLink) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("verifyLink", verifyLink);
        return templateEngine.process("mailTemplateReminder", context);
    }
}
