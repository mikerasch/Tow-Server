package edu.uwp.appfactory.tow.entities;

import lombok.Data;

@Data
public class CountDTO {
    private long PDUsers;
    private long TCUsers;
    private long totalUsers;
    public CountDTO(long PDUsers, long TCUsers, long totalUsers){
        this.PDUsers = PDUsers;
        this.TCUsers = TCUsers;
        this.totalUsers = totalUsers;
    }
}
