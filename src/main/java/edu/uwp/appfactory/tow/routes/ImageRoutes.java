package edu.uwp.appfactory.tow.routes;


import edu.uwp.appfactory.tow.requestObjects.FileRequest;
import edu.uwp.appfactory.tow.requestObjects.SetLocationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import edu.uwp.appfactory.tow.controllers.FileController;
import java.io.IOException;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/Images")
public class ImageRoutes {

    private final FileController fileController;
    public ImageRoutes( FileController fileController) {
        this.fileController = fileController;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody byte[] data)throws IOException {
        try{
            fileController.Upload(data);
                return  ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (IOException e) {
           return  ResponseEntity.status(400).body("Error" + e);
        }

    }


//    @PreAuthorize("hasRole('DRIVER')")
//    @PatchMapping("/my-location")
//    public ResponseEntity<?> setLocation(@RequestHeader("Authorization") final String jwtToken,
//                                         @RequestBody FileRequest fileRequest) {
//        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
//        System.out.println("lat: " + setRequest.getLatitude() + " long: " + setRequest.getLongitude() + " active: " + setRequest.isActive());
//        return locationController.setLocation(setRequest.getLatitude(), setRequest.getLongitude(), setRequest.isActive(), userUUID)
//                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
//                : ResponseEntity.status(400).body("Error");
//    }





    @GetMapping("/retrieve")
    public byte[] ImageRoutes(UUID id) {
        byte[] image = new byte[0];
        fileController.findById(id);
        return image;
    }
}

