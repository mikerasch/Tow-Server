package edu.uwp.appfactory.tow.routes;


import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.controllers.FileController;
import edu.uwp.appfactory.tow.requestObjects.FileRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/Images")
public class ImageRoutes {

    private final FileController fileController;
    private final JwtUtils jwtUtils;

    public ImageRoutes(FileController fileController, JwtUtils jwtUtils) {
        this.fileController = fileController;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody byte[] data) throws IOException {
        try {
            fileController.Upload(data);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(400).body("Error" + e);
        }

    }

    @PostMapping("/upload-multipart")
    public ResponseEntity<?> uploadMultipart(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            byte[] data = file.getBytes();
            fileController.Upload(data);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(400).body("Error" + e);
        }

    }

    @PostMapping("/upload-base64")
    public ResponseEntity<?> uploadMultipart(@RequestBody FileRequest file) throws IOException {
        try {
            String x = file.getImage();
            String y = x.substring(22);
            byte[] data = Base64.getMimeDecoder().decode(y);

            fileController.Upload(data);
            //System.out.println(Arrays.toString(data));

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error" + e);
        }

    }


    @PostMapping("/upload-base64-jwt")
    public ResponseEntity<?> uploadBase64Jwt(@RequestHeader final String jwtToken,
                                             @RequestBody FileRequest file) throws IOException {
        try {

            String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
            String x = file.getImage();
            String y = x.substring(22);
            byte[] data = Base64.getMimeDecoder().decode(y);

            fileController.Uploadjwt(data, userUUID);
            //System.out.println(Arrays.toString(data));

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error" + e);
        }

    }

    @PostMapping("/upload-base64-jwt-name")
    public ResponseEntity<?> uploadBase64JwtName(@RequestHeader final String jwtToken,
                                                 @RequestBody FileRequest file) throws IOException {
        try {

            String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
            String x = file.getImage();
            String y = x.substring(22);
            byte[] data = Base64.getMimeDecoder().decode(y);

            fileController.Uploadjwt(data, userUUID);
            //System.out.println(Arrays.toString(data));

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error" + e);
        }

    }


//    @GetMapping("/retrieve")
//    public ResponseEntity<?> retrieve(@RequestHeader final String jwtToken) {
//
//
//        byte[] data = fileController.findById(id).getData();
//        data = Base64.getMimeEncoder().encode(data);
//        return ResponseEntity.ok(data);
//
//    }


    @GetMapping("/retrieve-jwt")
    public ResponseEntity<?> retrieveJwt(@RequestHeader UUID id) {


        byte[] data = fileController.findById(id).getData();
        data = Base64.getMimeEncoder().encode(data);
        return ResponseEntity.ok(data);

    }

}

