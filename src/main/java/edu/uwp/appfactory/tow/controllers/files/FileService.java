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

/**
 * File Service class for business logic in all file operations to DB.
 */
@Service
public class FileService {
    private FileRepository fileRepository;
    private JwtUtils jwtUtils;
    private UsersRepository usersRepository;

    public FileService(FileRepository fileRepository, JwtUtils jwtUtils, UsersRepository usersRepository) {
        this.fileRepository = fileRepository;
        this.jwtUtils = jwtUtils;
        this.usersRepository = usersRepository;
    }
    /**
     * Stores the requested file in the database if the file extension is valid and if the user is authorized.
     * Compresses the file for storage benefit.
     * @param multipartFile - multipartFile from the client to be stored in the db
     * @param jwtToken - jwtToken for authentication
     * @return 200 OK if stored, else 400 or 401
     */
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

    /**
     * Retrieves the file from the database. Will ensure file belongs to user by
     * checking the UUID. Decompresses the file before returning.
     * @param filename - filename to search for
     * @param jwtToken - jwtToken for authentication
     * @return - ByteArrayResource of file
     */
    public ResponseEntity<ByteArrayResource> retrieveFile(String filename, String jwtToken) {
        Optional<File> file = fileRepository.findByFilenameAndUserUUID(filename, UUID.fromString(jwtUtils.getUUIDFromJwtToken(jwtToken)));
        if(file.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No file by that name exists.");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(file.get().getType()))
                .body(new ByteArrayResource(decompressBytes(file.get().getData())));
    }

    /**
     * Checks to see if uuid exists in the database.
     * @param uuid - String uuid
     * @return true if exists, false otherwise
     */
    public boolean isValidUser(String uuid){
        return usersRepository.existsById(UUID.fromString(uuid));
    }
}