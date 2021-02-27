package edu.uwp.appfactory.tow.routes;


import edu.uwp.appfactory.tow.controllers.FileController;
import edu.uwp.appfactory.tow.entities.FileDB;
import edu.uwp.appfactory.tow.exceptions.InvalidExtensionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/images")
public class ImageRoutes {

    private final FileController fileController;

    public ImageRoutes(FileController fileController) {
        this.fileController = fileController;
    }

    @PostMapping("/upload-multipart")
    public ResponseEntity<FileDB> uploadMultipart(@RequestHeader("Authorization") final String jwtToken,
                                                  @RequestParam("file") MultipartFile file) {
        try {
            fileController.uploadMultipart(file, jwtToken);
            return ResponseEntity
                    .status(OK)
                    .build();
        } catch (InvalidExtensionException | IOException e) {
            return ResponseEntity.status(BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/retrieve")
    public ResponseEntity<?> retrieveJwt(@RequestHeader("Authorization") final String jwtToken) {
        return ResponseEntity.ok(fileController.getFile(jwtToken).getData());
    }
}

