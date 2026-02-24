package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    // MessageService에 의존성 추가
    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    // 메세지를 만들때, 검증
    @Override
    public Message create(Message message) {
        if (userService.findById(message.getUserId()) == null) {
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }

        if (channelService.findById(message.getChannelId()) == null) {
            throw new IllegalStateException("존재하지 않는 채널입니다.");
        }

        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
