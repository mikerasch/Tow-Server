package edu.uwp.appfactory.tow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileDB {


    @Id
    private UUID id;

    @NotBlank
    private UUID useruuid;

    private String name;

    private String type;

    private byte[] data;


    public FileDB(String name, String type, byte[] data, UUID useruuid) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.useruuid = useruuid;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUseruuid() {
        return useruuid;
    }

    public void setUseruuid(UUID useruuid) {
        this.useruuid = useruuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


    private Integer resetToken;
    private String resetDate;
    private String verifyDate;
    private Boolean verEnabled;
    private String verifyToken;


}