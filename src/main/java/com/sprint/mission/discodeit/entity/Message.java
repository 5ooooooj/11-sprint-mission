package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Message extends BaseEntity {
    private final UUID userId;
    private final UUID channelId;
    private String content;

    public Message(UUID userId, UUID channelId, String content) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
        touch();
    }

    @Override
    public String toString() {
        return "Message{id = " + getId() +
                ", userId = " + userId +
                ", channelId = " + channelId +
                ", content = '" + content + '\'' +
                '}';
    }
}
