package edu.uwp.appfactory.tow.controllers.files;

import edu.uwp.appfactory.tow.services.files.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping
    public ResponseEntity<HttpStatus> uploadFile(@RequestParam(name = "file") MultipartFile multipartFile){
        return fileService.storeFile(multipartFile);
    }

    @GetMapping("download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> retrieveFile(@PathVariable String filename){
        return fileService.retrieveFile(filename);
    }
}
