package com.sprint.mission.discodeit.entity;

import lombok.Getter;

@Getter
public class Channel extends BaseEntity {
    private String channelName;
    private String description;

    public Channel(String channelName, String description) {
        super();
        this.channelName = channelName;
        this.description = description;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        touch();
    }

    public void updateDescriptrion(String description) {
        this.description = description;
        touch();
    }

    @Override
    public String toString() {
        return "Channel{id = " + getId() +
                ", channelName = '" + channelName + '\'' +
                ", description = '" + description + '\'' +
                '}';
    }
}
