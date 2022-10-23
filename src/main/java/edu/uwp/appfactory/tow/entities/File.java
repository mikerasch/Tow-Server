package edu.uwp.appfactory.tow.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table("file_db")
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    private UUID id;
    private UUID user_uuid;
    private String type;
    private String filename;
    private byte[] data;
}
