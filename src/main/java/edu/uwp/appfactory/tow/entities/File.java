package edu.uwp.appfactory.tow.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table("file_db")
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    @Persistent
    private UUID id;
    private String type;
    private byte[] data;
}
