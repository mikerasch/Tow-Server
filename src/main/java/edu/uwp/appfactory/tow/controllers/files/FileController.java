package edu.uwp.appfactory.tow.controllers.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class contains the Routes responsible for file transfer.
 * Included paths are:
 * post - /file
 * get - /file/{filename}
 */
@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    /**
     * Uploads the file to the database.
     * The file extensions must either be jpg, png, or jpeg.
     * @param multipartFile - multipartFile which the client provides
     * @param jwtToken - jwtToken for authentication
     * @return 200 OK if successful, 400 if invalid extension/invalid file, else 401
     */
    @PostMapping
    @PreAuthorize("hasRole('PDADMIN') or hasRole('PDUSER') or hasRole('TCADMIN') or hasRole('TCUSER')")
    public ResponseEntity<HttpStatus> uploadFile(@RequestParam(name = "file") MultipartFile multipartFile,
                                                 @RequestHeader("Authorization") final String jwtToken){
        return fileService.storeFile(multipartFile,jwtToken);
    }

    /**
     * Retrieves the requested file from the user.
     * @param filename - file to be looked up in the db
     * @param jwtToken - jwtToken for authentication
     * @return ByteArrayResource media file if successful, else bad request
     */
    @GetMapping("download/{filename:.+}")
    @PreAuthorize("hasRole('PDADMIN') or hasRole('PDUSER') or hasRole('TCADMIN') or hasRole('TCUSER')")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> retrieveFile(@PathVariable String filename,
                                                          @RequestHeader("Authorization") final String jwtToken){
        return fileService.retrieveFile(filename,jwtToken);
    }
}
