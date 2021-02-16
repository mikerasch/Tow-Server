package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.entities.FileDB;
import edu.uwp.appfactory.tow.repositories.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@EnableAutoConfiguration
@Controller
public class FileController {

    private final FileDBRepository fileDBRepository;

    @Autowired
    public FileController(FileDBRepository fileDBRepository) {
        this.fileDBRepository = fileDBRepository;
    }


    public FileDB findById(UUID Id) {
        Optional<FileDB> fileDB = fileDBRepository.findById(Id);
        return fileDB.orElse(null);
    }





    public FileDB Upload(byte[] data) throws IOException {

        FileDB FileDB = new FileDB(data);

        return fileDBRepository.save(FileDB);
    }

    public FileDB Uploadjwt(byte[] data,String userUUID) throws IOException {

        FileDB FileDB = new FileDB(data, userUUID);

        return fileDBRepository.save(FileDB);
    }

//    public <Multipartfile> FileDB UploadMultipart(Multipartfile file) throws IOException {
//
//        byte[] data = file.getdada
//        FileDB FileDB = new FileDB(data);
//
//        return fileDBRepository.save(FileDB);
//    }


//    public FileDB getFile(UUID id) {
//        return fileDBRepository.findById(id).get();
//    }

}


