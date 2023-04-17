package edu.uwp.appfactory.tow.controllers.email;

import edu.uwp.appfactory.tow.requestobjects.email.SupportEmail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {
    private final AsyncEmailService asyncEmailService;
    public EmailController(AsyncEmailService asyncEmailService) {
        this.asyncEmailService = asyncEmailService;
    }

    /**
     * Sends a customer support email asynchronously with the given information.
     *
     * @param supportEmail the SupportEmail object containing information about the email to be sent
     * @return a ResponseEntity with a success message if the email was sent successfully
     */
    @PostMapping("/support/send")
    @PreAuthorize("hasAnyRole('PDADMIN','PDUSER','TCADMIN','TCUSER','SPADMIN','DRIVER')")
    public ResponseEntity<String> sendCustomerSupportEmail(@RequestBody SupportEmail supportEmail) {
        asyncEmailService.submitCustomerServiceEmail(supportEmail);
        return ResponseEntity.ok("Email has been sent.");
    }
}
