package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.entities.FileDB;
import edu.uwp.appfactory.tow.exceptions.InvalidExtensionException;
import edu.uwp.appfactory.tow.repositories.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Controller
public class FileController {

    private final FileDBRepository fileDBRepository;
    private final JwtUtils jwtUtils;

    private final List<String> allowedExtensions = new ArrayList<>() {
        {
            add("image/png");
            add("image/jpg");
            add("image/jpeg");
        }
    };

    @Autowired
    public FileController(FileDBRepository fileDBRepository, JwtUtils jwtUtils) {
        this.fileDBRepository = fileDBRepository;
        this.jwtUtils = jwtUtils;
    }

    public void uploadMultipart(MultipartFile file, String jwtToken) throws InvalidExtensionException, IOException {
        if (!allowedExtensions.contains(file.getContentType())) {
            throw new InvalidExtensionException("File Extension not allowed");
        }
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        FileDB fileDB = FileDB.builder()
                .id(UUID.fromString(userUUID))
                .data(file.getBytes())
                .build();
        fileDBRepository.save(fileDB);
    }


    public FileDB getFile(String jwtToken) {
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        return fileDBRepository.findById(UUID.fromString(userUUID)).orElse( new FileDB());
    }

}


