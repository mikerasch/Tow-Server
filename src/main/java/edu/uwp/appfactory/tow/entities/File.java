package edu.uwp.appfactory.tow.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "file_name", nullable = false)
    private String filename;
    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "file_data", nullable = false, length = 200000)
    private byte[] data;
    @Column(name = "date", nullable = false)
    private Timestamp date;
    @Column(name = "location", nullable = false)
    private String location;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_join", referencedColumnName = "id")
    private Users user;
    public File(Long id, String type, String filename, byte[] data, Timestamp timestamp, String location){
        this.userId = id;
        this.type = type;
        this.filename = filename;
        this.data = data;
        this.date = timestamp;
        this.location = location;
    }
    public File(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        File file = (File) o;
        return id != null && Objects.equals(id, file.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
