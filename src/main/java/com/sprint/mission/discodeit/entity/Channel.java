package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel extends BaseEntity {
    private String channelName;
    private String description;
    private final ChannelType type;
    private final List<UUID> participantIds;

    // 누구나 사용 가능하고 이름과 설명으로 채널 보여짐
    public Channel(String channelName, String description) {
        super();
        this.channelName = channelName;
        this.description = description;
        this.type = ChannelType.PUBLIC;
        this.participantIds = new ArrayList<>();
    }

    // DM, 그룹채팅 같이 참여자만 볼 수 있음
    // 그래서 생성자를 다르게 설계
    public Channel(List<UUID> participantIds){
        super();
        this.channelName = null;
        this.description = null;
        this.type = ChannelType.PRIVATE;
        this.participantIds = new ArrayList<>(participantIds);
    }

    public void update(String channelName, String description) {
        if(type == ChannelType.PRIVATE){
            throw new IllegalStateException("PRIVATE 채널은 수정 불가합니다.");
        }
        this.channelName = channelName;
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
