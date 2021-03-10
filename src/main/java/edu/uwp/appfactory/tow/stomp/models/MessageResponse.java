package edu.uwp.appfactory.tow.stomp.models;

import java.util.Date;

public class MessageResponse {

    private String content;
    private String groupName;
    private Date sentTimestamp;

    public MessageResponse() {
    }

    public MessageResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getSentTimestamp() {
        return sentTimestamp;
    }

    public void setSentTimestamp(Date sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }
}
