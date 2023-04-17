package edu.uwp.appfactory.tow.controllers.files;

import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Uploads a file to the server with the given information and returns a ResponseEntity with the HTTP status.
     *
     * @param multipartFile the file to upload
     * @param location the location to store the file
     * @param userDetails the authenticated user making the request
     * @return a ResponseEntity with the HTTP status
     */
    @PostMapping
    @PreAuthorize("hasRole('PDADMIN') or hasRole('PDUSER') or hasRole('TCADMIN') or hasRole('TCUSER') or hasRole('DRIVER')")
    @RequestMapping("/upload")
    public ResponseEntity<HttpStatus> uploadFile(@RequestParam(name = "file") MultipartFile multipartFile,
                                                 @RequestHeader(value = "location") String location,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return fileService.storeFile(multipartFile,userDetails,location);
    }

    /**
     * Retrieves a file with the given filename and returns it as a ByteArrayResource in a ResponseEntity.
     *
     * @param filename the filename of the file to retrieve
     * @param userDetails the authenticated user making the request
     * @return a ResponseEntity containing a ByteArrayResource with the file contents
     */
    @GetMapping("download/{filename:.+}")
    @PreAuthorize("hasRole('PDADMIN') or hasRole('PDUSER') or hasRole('TCADMIN') or hasRole('TCUSER') or hasRole('DRIVER')")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> retrieveFile(@PathVariable String filename,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        return fileService.retrieveFile(filename,userDetails);
    }
}
