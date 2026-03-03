package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public FileMessageService(MessageRepository messageRepository, UserService userService, ChannelService channelService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(Message message) {
        if (userService.findById(message.getUserId()) == null) {
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }
        if (channelService.findById(message.getChannelId()) == null) {
            throw new IllegalStateException("존재하지 않는 채널입니다.");
        }
        return messageRepository.save(message);
    }

    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(Message message) {
        if (userService.findById(message.getUserId()) == null) {
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }
        if (channelService.findById(message.getChannelId()) == null) {
            throw new IllegalStateException("존재하지 않는 채널입니다.");
        }
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }
}
