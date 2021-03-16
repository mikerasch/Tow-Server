package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.entities.File;
import edu.uwp.appfactory.tow.exceptions.InvalidExtensionException;
import edu.uwp.appfactory.tow.mappers.FileMapper;
import edu.uwp.appfactory.tow.repositories.FileRepository;
import edu.uwp.appfactory.tow.responseObjects.FileResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
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

    private final FileRepository fileRepository;
    private final JwtUtils jwtUtils;
    private final FileMapper fileMapper;

    private final List<String> allowedExtensions = new ArrayList<>() {
        {
            add("image/png");
            add("image/jpg");
            add("image/jpeg");
        }
    };

    @Autowired
    public FileController(FileRepository fileRepository, JwtUtils jwtUtils, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.jwtUtils = jwtUtils;
        this.fileMapper = fileMapper;
    }

    public void upload(MultipartFile file, String jwtToken) throws InvalidExtensionException, IOException {
        if (!allowedExtensions.contains(file.getContentType())) {
            throw new InvalidExtensionException("File Extension not allowed");
        }
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        File fileDB = File.builder()
                .id(UUID.fromString(userUUID))
                .type(file.getContentType())
                .data(file.getBytes())
                .build();
        fileRepository.save(fileDB);
    }


    public FileResponse get(String jwtToken) {
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        Optional<File> file = fileRepository.findById(UUID.fromString(userUUID));
        return file.map(fileMapper::map).orElse(null);
    }

}
