package edu.uwp.appfactory.tow.services.email;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * This class is responsible for constructing the emails contents that asyncemail will send to the user.
 */
@Service
public class ContentBuilderService {
    private final TemplateEngine templateEngine;
    private static final String USERNAME = "userName";
    public ContentBuilderService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Builds the password template to be used in sending an email.
     * @param userName username to be processed in the email
     * @param token token to be processed in the email
     * @return result of the template with username and the token
     */
    public String buildPasswordEmail(String userName, int token) {
        Context context = new Context();
        context.setVariable(USERNAME, userName);
        context.setVariable("token", token);
        return templateEngine.process("mailTemplatePassword", context);
    }

    /**
     * Builds the email tempalte to be used in sending an email
     * @param userName username to be processed in the email
     * @param verifyLink link to be processed in the email
     * @return result of the template with username and verification link
     */
    public String buildVerifyEmail(String userName, String verifyLink) {
        Context context = new Context();
        context.setVariable(USERNAME, userName);
        context.setVariable("verifyLink", verifyLink);
        return templateEngine.process("mailTemplateVerify", context);
    }

    /**
     * Contructs the email used by the chron job to send a user who has not completed
     * the registration process.
     * @param userName - name of the user that needs reminding
     * @param verifyLink - generated link that will route a user to the password reset page
     * @return returns the generated template with the username and verification link
     */
    public String buildReminderEmail(String userName, String verifyLink) {
        Context context = new Context();
        context.setVariable(USERNAME, userName);
        context.setVariable("verifyLink", verifyLink);
        return templateEngine.process("mailTemplateReminder", context);
    }
}
