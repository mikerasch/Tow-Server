package edu.uwp.appfactory.tow.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "requests")
public class Requests {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long requestId;
    @Column(name = "driver_id", nullable = false)
    private Long driverId;
    @Column(name = "tow_truck_id", nullable = false)
    private Long towTruckId;
    @Column(name = "request_date_time")
    private Timestamp requestDateTime;
    // todo add some sort of relationship
}
