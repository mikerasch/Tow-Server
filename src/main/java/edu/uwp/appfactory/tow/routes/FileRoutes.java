package edu.uwp.appfactory.tow.routes;


import edu.uwp.appfactory.tow.controllers.FileController;
import edu.uwp.appfactory.tow.entities.File;
import edu.uwp.appfactory.tow.exceptions.InvalidExtensionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

/**
 * This class contains routes that can take in and then send back file information when requested.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/images")
public class FileRoutes {

    private final FileController fileController;

    public FileRoutes(FileController fileController) {
        this.fileController = fileController;
    }


    /**
     *      * Takes in the users jwt token and a multipart file. The multipart file contains the image or file itself
     *      * and various metadata regarding the image(you'll notice postgres knows what type of file is being stored).
     *      * The token allows us to extract the users UUID which we use as a primary key and the photos own UUID. This should
     *      * be changed to something less.... wrong if possible.
     * @param jwtToken used to extract UUID for user association
     * @param file multipart file that contains data and metadata
     * @return a success or failure method
     */
    @PostMapping("")
    public ResponseEntity<File> upload(@RequestHeader("Authorization") final String jwtToken,
                                       @RequestParam("file") MultipartFile file) {
        try {
            fileController.upload(file, jwtToken);
            return ResponseEntity
                    .status(OK)
                    .build();
        } catch (InvalidExtensionException | IOException e) {
            return ResponseEntity.status(BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * Get route that returns the photo based off of the UUID of the requester.
     * @param jwtToken used to find the photo using the UUID
     * @return the photo itself.
     */
    @GetMapping("")
    public ResponseEntity<?> get(@RequestHeader("Authorization") final String jwtToken) {
        return ResponseEntity.ok(fileController.get(jwtToken));
    }


    /**
     * At one point sent in the uuid from mobile as well. may change back
     * @param jwtToken
     * @param UUID
     * @return
     */
    @GetMapping("/tca")
    public ResponseEntity<?> get(@RequestHeader("Authorization") final String jwtToken,
                                 @RequestHeader("UUID") final UUID UUID) {
        return ResponseEntity.ok(fileController.get(jwtToken));
    }
}

