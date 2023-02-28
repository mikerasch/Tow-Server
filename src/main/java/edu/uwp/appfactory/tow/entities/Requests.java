package edu.uwp.appfactory.tow.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class Requests {
    @Id
    public UUID request_id;
    public UUID driver_id;
    public UUID tow_truck_id;
    public Timestamp request_date;

}
