package edu.uwp.appfactory.tow.routes;


import edu.uwp.appfactory.tow.entities.FileDB;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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



    @GetMapping("/retrieve")
    public ResponseEntity<?> retrieve(@RequestHeader UUID id) {
        FileDB data = fileController.findById(id);
        return ResponseEntity.ok(data);
    }
}

