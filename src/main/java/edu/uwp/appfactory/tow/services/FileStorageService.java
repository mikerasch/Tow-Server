//package edu.uwp.appfactory.tow.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//import edu.uwp.appfactory.tow.entities.FileDB;
//import edu.uwp.appfactory.tow.repositories.FileDBRepository;
//
//import java.io.IOException;
//import java.util.UUID;
//import java.util.stream.Stream;
//
//
//
//@Service
//public class FileStorageService {
//
//    @Autowired
//    private FileDBRepository fileDBRepository;
//
//    @Async
//    public FileDB store(String name, String type,  byte[] data, UUID useruuid) throws IOException {
//
//        FileDB FileDB = new FileDB(name, type, data, useruuid);
//
//        return fileDBRepository.save(FileDB);
//    }
//
//    public FileDB getFile(UUID id) {
//        return fileDBRepository.findById(id).get();
//    }
//
////    public Stream<FileDB> getAllFiles() {
////        return fileDBRepository.findAll().stream();
////    }
//}
