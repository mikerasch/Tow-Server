package edu.uwp.appfactory.tow.controllers.files;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Utility class for handling file operations.
 */
public class HandleFileOperationsUtil {

    private HandleFileOperationsUtil() throws IllegalStateException {
        throw new IllegalStateException();
    }
    private static final HashSet<String> validFileExtensions = new HashSet<>(List.of(
       "jpg","png","jpeg"
    ));

    /**
     * Ensures content type is a valid extension.
     * Valid extension types are: jpg, png, jpeg.
     *
     * @param contentType - content type of file
     * @return true if valid, false otherwise
     */
    public static boolean isValidFileExtension(String contentType){
        String extension = getExtension(contentType);
        return validFileExtensions.contains(extension);
    }

    /**
     * Retrieves the extension of the content type.
     *
     * @param contentType - content type to split
     * @return - extension
     */
    private static String getExtension(String contentType) {
        return contentType.split("/")[1];
    }

    /**
     * Compresses the byte array by using Deflater.
     *
     * @param bytes - byte array to be compressed
     * @return - new compressed byte array
     */
    public static byte[] compressBytes(byte[] bytes){
        Deflater deflater = new Deflater();
        deflater.setInput(bytes);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()){
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        return outputStream.toByteArray();
    }

    /**
     * Decompresses the byte array using Inflater.
     *
     * @param bytes - byte array to be compressed
     * @return - new decompressed byte array
     */
    public static byte[] decompressBytes(byte[] bytes){
        Inflater inflater = new Inflater();
        inflater.setInput(bytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
        byte[] buffer = new byte[1024];
        try{
            while(!inflater.finished()){
                int count = inflater.inflate(buffer);
                outputStream.write(buffer,0,count);
            }
        } catch(DataFormatException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"File corrupted, could not upload");
        }
        return outputStream.toByteArray();
    }
}
