package edu.uwp.appfactory.tow.controllers.spAdmin;

import edu.uwp.appfactory.tow.requestObjects.rolerequest.SuperAdminRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/spadmins")
public class SuperAdminController {
    @Autowired
    private SuperAdminService superAdminService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<TestVerifyResponse> registerSuperAdmin(@RequestBody SuperAdminRequest spAdminRequest){
        return superAdminService.register(spAdminRequest);
    }


}
