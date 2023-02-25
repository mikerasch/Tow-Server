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

    @PostMapping("/support/send")
    @PreAuthorize("hasAnyRole('PDADMIN','PDUSER','TCADMIN','TCUSER','SPADMIN','DRIVER')")
    public ResponseEntity<String> sendCustomerSupportEmail(@RequestBody SupportEmail supportEmail) {
        asyncEmailService.submitCustomerServiceEmail(supportEmail);
        return ResponseEntity.ok("Email has been sent.");
    }
}
