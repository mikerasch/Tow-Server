package edu.uwp.appfactory.tow.controllers.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping
    @PreAuthorize("hasRole('PDADMIN') or hasRole('PDUSER') or hasRole('TCADMIN') or hasRole('TCUSER')")
    public ResponseEntity<HttpStatus> uploadFile(@RequestParam(name = "file") MultipartFile multipartFile,
                                                 @RequestHeader("Authorization") final String jwtToken){
        return fileService.storeFile(multipartFile,jwtToken);
    }

    @GetMapping("download/{filename:.+}")
    @PreAuthorize("hasRole('PDADMIN') or hasRole('PDUSER') or hasRole('TCADMIN') or hasRole('TCUSER')")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> retrieveFile(@PathVariable String filename,
                                                          @RequestHeader("Authorization") final String jwtToken){
        return fileService.retrieveFile(filename,jwtToken);
    }
}
