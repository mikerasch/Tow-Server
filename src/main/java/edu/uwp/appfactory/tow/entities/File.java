package edu.uwp.appfactory.tow.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Table("file_db")
public class File {
    @Id
    private UUID id;
    private UUID user_uuid;
    private String type;
    private String filename;
    private byte[] data;
    private Timestamp date;
    private String location;
    public File(UUID user_uuid, String type, String filename, byte[] data, Timestamp timestamp, String location){
        this.user_uuid = user_uuid;
        this.type = type;
        this.filename = filename;
        this.data = data;
        this.date = timestamp;
        this.location = location;
    }
    public File(){

    }
}
