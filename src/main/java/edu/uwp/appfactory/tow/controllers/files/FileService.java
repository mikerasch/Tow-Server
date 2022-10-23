package edu.uwp.appfactory.tow.controllers.files;


import edu.uwp.appfactory.tow.entities.File;
import edu.uwp.appfactory.tow.repositories.FileRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import static edu.uwp.appfactory.tow.controllers.files.HandleFileOperationsUtil.*;
@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UsersRepository usersRepository;
    public ResponseEntity<HttpStatus> storeFile(MultipartFile multipartFile, String jwtToken) {
        boolean isValidExtension = isValidFileExtension(multipartFile.getContentType());
        if(!isValidExtension){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid extension");
        }
        String uuid = jwtUtils.getUUIDFromJwtToken(jwtToken);
        if(!isValidUser(uuid)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User does not exist");
        }
        try{
            File file = new File();
            file.setUser_uuid(UUID.fromString(uuid));
            file.setType(multipartFile.getContentType());
            file.setData(compressBytes(multipartFile.getBytes()));
            file.setFilename(multipartFile.getOriginalFilename());
            fileRepository.save(file);
            return ResponseEntity.ok().build();
        } catch(IOException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Could not read file");
        }
    }

    public ResponseEntity<ByteArrayResource> retrieveFile(String filename, String jwtToken) {
        Optional<File> file = fileRepository.findByFilenameAndUserUUID(filename, UUID.fromString(jwtUtils.getUUIDFromJwtToken(jwtToken)));
        if(file.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No file by that name exists.");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(new ByteArrayResource(decompressBytes(file.get().getData())));
    }

    public boolean isValidUser(String uuid){
        return usersRepository.existsById(UUID.fromString(uuid));
    }
}
