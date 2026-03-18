package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User extends BaseEntity{
    private String userName;
    // 주민번호나, email 같은 객체로 ..
    private String statusMessage;
    private UUID profileId;

    public User(String userName, String statusMessage) {
        super();
        this.userName = userName;
        this.statusMessage = statusMessage;
        this.profileId = null;
    }

    public void updateUserName(String userName){
        this.userName = userName;
        touch();
    }

    public void updateStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        touch();
    }

    public void updateProfileId(UUID profileId){
        this.profileId = profileId;
        touch();
    }

    @Override
    public String toString() {
        return "User{id=" + getId()
                + ", userName='" + userName + '\''
                + ", statusMessage='" + statusMessage + '\''
                + "}";
    }
}
