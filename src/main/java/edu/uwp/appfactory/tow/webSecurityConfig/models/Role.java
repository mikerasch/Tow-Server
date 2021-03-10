package edu.uwp.appfactory.tow.webSecurityConfig.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role {

    @Id
    private Integer id;
    private ERole name;
}