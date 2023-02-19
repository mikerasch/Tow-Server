package edu.uwp.appfactory.tow.controllers.files;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static edu.uwp.appfactory.tow.controllers.files.HandleFileOperationsUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HandleFileOperationsUtilTest {

    @Test
    void isValidFileExtensionShouldReturnTrue() {
        // given
        List<String> extensions = List.of(
                "image/jpg","image/png","image/jpeg"
        );

        // when
        boolean isValid = true;
        for(String extension: extensions){
            isValid = isValidFileExtension(extension);
            if(!isValid){
                break;
            }
        }

        // then
        assertTrue(isValid);
    }

    @Test
    void isNotValidFileExtensionShouldReturnFalse(){
        // given
        List<String> extensions = List.of(
                "text/plain",
                "application/json",
                "application/x-spring-tuple",
                "application/avro"
        );
        List<Boolean> isFalse = new ArrayList<>();

        // when
        for(String extension: extensions){
            isFalse.add(isValidFileExtension(extension));
        }

        // then
        assertThat(isFalse).containsOnly(false);
    }

    // PNG by default is compressed, JPG is a good test example
    @Test
    void compressBytesShouldReturnSmallerSize() throws IOException {
        // given
        byte[] data = Files.readAllBytes(Path.of("src/test/resources/testcompress.jpg"));

        // when
        byte[] compressedData = compressBytes(data);

        // then
        assertThat(data).hasSizeGreaterThan(compressedData.length);
    }

    @Test
    void decompressBytesShouldReturnLargerSize() throws IOException {
        // given
        byte[] data = Files.readAllBytes(Path.of("src/test/resources/testcompress.jpg"));
        byte[] compressedData = compressBytes(data);

        // when
        byte[] decompressData = decompressBytes(compressedData);

        // then
        assertThat(decompressData).hasSizeGreaterThan(compressedData.length);
    }
}