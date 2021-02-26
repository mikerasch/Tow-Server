package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.entities.FileDB;
import edu.uwp.appfactory.tow.repositories.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;


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


//    public ResponseEntity<?> findByIdJwt(UUID Id, String useruuid) {
//        Optional<FileDB> fileDB = fileDBRepository.findById(Id);
//
//        if (fileDB.isPresent()) {
//            if (!fileDB.get().getUserUUID().equals(useruuid)) {
//                return ResponseEntity.status(400).body("NOT YO, PHO TO");
//            } else {
//                return ResponseEntity.ok(fileDB);
//            }
//        }else {
//            return ResponseEntity.status(410).body(null);
//        }
//    }

//    public FileDB findByIdJwt(UUID Id, String useruuid) {
//        Optional<FileDB> fileDB = fileDBRepository.findById(Id);
//
//        if (fileDB.isPresent()) {
//            if (!fileDB.get().getUserUUID().equals(useruuid)) {
//                return null;
//            } else {
//                return fileDB.orElse(null);
//            }
//        }else {
//            return null;
//        }
//    }


    public FileDB Upload(byte[] data) throws IOException {

        FileDB FileDB = new FileDB(data);

        return fileDBRepository.save(FileDB);
    }

    public FileDB UploadFinished(byte[] data, UUID userUUID, String name, String type) throws IOException {

        FileDB FileDB = new FileDB(data);

        return fileDBRepository.save(FileDB);
    }


    public FileDB UploadMulti(MultipartFile file) throws IOException {


        byte[] data = file.getBytes();
        FileDB FileDB = new FileDB(data);

        return fileDBRepository.save(FileDB);
    }

//    public FileDB Uploadjwt(byte[] data, String userUUID) throws IOException {
//
//        FileDB FileDB = new FileDB(data, userUUID);
//
//        return fileDBRepository.save(FileDB);
//    }

//    public <Multipartfile> FileDB UploadMultipart(Multipartfile file) throws IOException {

//        byte[] data = file.getdada
//        FileDB FileDB = new FileDB(data);
//
//        return fileDBRepository.save(FileDB);
//    }


//    public FileDB getFile(UUID id) {
//        return fileDBRepository.findById(id).get();
//    }

}


