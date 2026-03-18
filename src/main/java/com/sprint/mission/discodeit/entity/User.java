package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User extends BaseEntity{
    private String userName;
    private String statusMessage;
    private UUID profileId;
    private String email;
    private String password;

    public User(String userName, String email, String password, String statusMessage) {
        super();
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.statusMessage = statusMessage;
        this.profileId = null;
    }

    public void updateUserName(String userName){
        this.userName = userName;
        touch();
    }

    public void updateEmail(String email){
        this.email = email;
        touch();
    }

    public void updatePassword(String password){
        this.password = password;
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
