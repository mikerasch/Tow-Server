package edu.uwp.appfactory.tow.controllers;

//import edu.uwp.appfactory.tow.entities.File;
import edu.uwp.appfactory.tow.exceptions.InvalidExtensionException;
import edu.uwp.appfactory.tow.mappers.FileMapper;
import edu.uwp.appfactory.tow.repositories.FileRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * this class is responsible for storing and retrieving the files that come in through the file routes.
 * It limtis the type of files and the size. The system automatically limits the number of files to one by
 * replacing the current photo with the newest entry.
 */
@Controller
public class FileController {

    @Value("${upload.path}")
    private String uploadPath;




    private final FileRepository fileRepository;
    private final JwtUtils jwtUtils;
    private final FileMapper fileMapper;

    /**
     * Limits the type of file coming in this route to the list specified. otherwise pdfs, or any other file could
     * be sent in a nd stored.
     */
    private final List<String> allowedExtensions = new ArrayList<>() {
        {
            add("image/png");
            add("image/jpg");
            add("image/jpeg");
        }
    };


    @Autowired
    public FileController( FileRepository fileRepository, JwtUtils jwtUtils, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.jwtUtils = jwtUtils;
        this.fileMapper = fileMapper;
    }

    /**
     * Method that stores the file into the postgres database in a binary array.  UUID is the primary key, its wrong so wrong
     * please change to be less wrong in the future.
     * @param file the multipart file being stored
     * @param jwtToken the token of the sender, used to associate the file with a user in the table.
     * @throws InvalidExtensionException if the filetype is not of the predetermined options
     * @throws IOException there was an error storing the file.
     */
    public void upload(MultipartFile file, String jwtToken) throws InvalidExtensionException, IOException {
//        if (!allowedExtensions.contains(file.getContentType())) {
//            throw new InvalidExtensionException("File Extension not allowed");
//        }
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);

        String base_path = uploadPath;
        File pathAsFile = new File(base_path + "/" + userUUID);

        pathAsFile.mkdir();
//        if (!Files.exists(Paths.get(pathAsFile))) {
//            pathAsFile.mkdir();
//        }


        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadPath  + userUUID + "/" + file.getOriginalFilename());
            Files.write(path, bytes);

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }



    }


    /**
     * retrieve the file based on the UUID of the jwt requesting
     * @param jwtToken used to extract the UUID primary key
     * @return returns the multipart file that contains the image
     */
    public Optional<MultipartFile> get(String jwtToken, String filename) throws FileNotFoundException {
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        String base_path = uploadPath;

        try {
            String pathAsFile = base_path + "/" + userUUID + "/" + filename;
//            FileOutputStream out = new FileOutputStream(pathAsFile);
//            File in = new File(pathAsFile);
            byte[] bytes = Files.readAllBytes(Paths.get(pathAsFile));
            Optional<MultipartFile> multipartFile = Optional.of(new MockMultipartFile(filename, bytes));
            return multipartFile;
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve the file. Error: " + e.getMessage());
        }

    }
//
//    public FileResponse get(String jwtToken, String uuid) {
//        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
//        Optional<File> file = fileRepository.findById(UUID.fromString(userUUID));
//        return file.map(fileMapper::map).orElse(null);
//    }

}
