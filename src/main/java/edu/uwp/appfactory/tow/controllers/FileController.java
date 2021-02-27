package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.entities.File;
import edu.uwp.appfactory.tow.exceptions.InvalidExtensionException;
import edu.uwp.appfactory.tow.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Controller
public class FileController {

    private final FileRepository fileRepository;
    private final JwtUtils jwtUtils;

    private final List<String> allowedExtensions = new ArrayList<>() {
        {
            add("image/png");
            add("image/jpg");
            add("image/jpeg");
        }
    };

    @Autowired
    public FileController(FileRepository fileRepository, JwtUtils jwtUtils) {
        this.fileRepository = fileRepository;
        this.jwtUtils = jwtUtils;
    }

    public void upload(MultipartFile file, String jwtToken) throws InvalidExtensionException, IOException {
        if (!allowedExtensions.contains(file.getContentType())) {
            throw new InvalidExtensionException("File Extension not allowed");
        }
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        File fileDB = File.builder()
                .id(UUID.fromString(userUUID))
                .data(file.getBytes())
                .build();
        fileRepository.save(fileDB);
    }


    public File get(String jwtToken) {
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        return fileRepository.findById(UUID.fromString(userUUID)).orElse(new File());
    }

}


