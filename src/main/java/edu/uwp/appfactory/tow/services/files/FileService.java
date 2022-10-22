package edu.uwp.appfactory.tow.services.files;


import edu.uwp.appfactory.tow.entities.File;
import edu.uwp.appfactory.tow.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    public ResponseEntity<HttpStatus> storeFile(MultipartFile multipartFile) {
        try{
            File file = new File();
            file.setUser_uuid(UUID.randomUUID());
            file.setType(multipartFile.getContentType());
            file.setData(multipartFile.getBytes());
            file.setFilename(multipartFile.getOriginalFilename());
            fileRepository.save(file);
            return ResponseEntity.ok().build();
        } catch(IOException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Could not read file");
        }
    }

    public ResponseEntity<ByteArrayResource> retrieveFile(String filename) {
        Optional<File> file = fileRepository.findByFilename(filename);
        if(file.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No file by that name exists.");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(new ByteArrayResource(file.get().getData()));
    }
}
