package edu.uwp.appfactory.tow.requestobjects.statistics;

import lombok.Data;

@Data
public class CountDTO {
    private long pdUsers;
    private long tcUsers;
    private long totalUsers;
    public CountDTO(long pdUsers, long tcUsers, long totalUsers){
        this.pdUsers = pdUsers;
        this.tcUsers = tcUsers;
        this.totalUsers = totalUsers;
    }
}
