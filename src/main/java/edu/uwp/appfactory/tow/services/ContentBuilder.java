package edu.uwp.appfactory.tow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 */
@Service
public class ContentBuilder {
    private final TemplateEngine templateEngine;

    /**
     *
     * @param templateEngine
     */
    @Autowired
    public ContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     *
     * @param userName
     * @param token
     * @return
     */
    public String buildEmail(String userName, int token) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("token", token);
        return templateEngine.process("mailTemplateCray", context);
    }
}
