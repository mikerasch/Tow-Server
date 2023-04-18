package edu.uwp.appfactory.tow.controllers.files;


import edu.uwp.appfactory.tow.entities.File;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.repositories.FileRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import static edu.uwp.appfactory.tow.controllers.files.HandleFileOperationsUtil.*;

/**
 * File Service class for business logic in all file operations to DB.
 */
@Service
@Slf4j
public class FileService {
    private FileRepository fileRepository;
    private UsersRepository usersRepository;

    public FileService(FileRepository fileRepository, UsersRepository usersRepository) {
        this.fileRepository = fileRepository;
        this.usersRepository = usersRepository;
    }
    /**
     * Stores the requested file in the database if the file extension is valid and if the user is authorized.
     * Compresses the file for storage benefit.
     *
     * @param multipartFile - multipartFile from the client to be stored in the db
     * @return 200 OK if stored, else 400 or 401
     */
    public ResponseEntity<HttpStatus> storeFile(MultipartFile multipartFile, UserDetailsImpl userDetails, String location) {
        boolean isValidExtension = isValidFileExtension(multipartFile.getContentType());
        if(!isValidExtension){
            log.warn("Error while storing multipart file. Expected a valid file extension but received {}", multipartFile.getContentType());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid extension");
        }
        Users user = usersRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user!"));
        String uuid = String.valueOf(user.getId());
        if(!isValidUser(uuid)){
            log.error("While storing multipart file, authentication could not be provided");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User does not exist");
        }
        try {
            File file = new File();
            file.setUserId(Long.parseLong(uuid));
            file.setType(multipartFile.getContentType());
            file.setData(compressBytes(multipartFile.getBytes()));
            file.setFilename(multipartFile.getOriginalFilename());
            file.setDate(new Timestamp(new Date().getTime()));
            file.setLocation(location);
            user.addFiles(file);
            usersRepository.save(user);
            log.debug("Saving new file {} to database", multipartFile.getOriginalFilename());
            return ResponseEntity.ok().build();
        } catch(IOException e){
            log.error("While reading file name {} extension {} it was unable to be read correctly", multipartFile.getOriginalFilename(), multipartFile.getContentType());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Could not read file");
        }
    }

    /**
     * Retrieves the file from the database. Will ensure file belongs to user by
     * checking the UUID. Decompresses the file before returning.
     *
     * @param filename - filename to search for
     * @return - ByteArrayResource of file
     */
    public ResponseEntity<ByteArrayResource> retrieveFile(String filename, UserDetailsImpl userDetails) {
        Users user = usersRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user!"));
        Optional<File> file = fileRepository.findByFilenameAndUserId(filename, user.getId());
        if(file.isEmpty()){
            log.warn("While retrieving filename {}, it could not be located", filename);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No file by that name exists.");
        }
        log.debug("Retrieving file {}", filename);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(file.get().getType()))
                .body(new ByteArrayResource(decompressBytes(file.get().getData())));
    }

    /**
     * Checks to see if uuid exists in the database.
     *
     * @param uuid - String uuid
     * @return true if exists, false otherwise
     */
    public boolean isValidUser(String uuid){
        return usersRepository.existsById(Long.parseLong(uuid));
    }
}
