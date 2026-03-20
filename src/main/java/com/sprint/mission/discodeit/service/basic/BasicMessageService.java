package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

//    public BasicMessageService(
//            MessageRepository messageRepository,
//            UserRepository userRepository,
//            ChannelRepository channelRepository
//    ) {
//        this.messageRepository = messageRepository;
//        this.userRepository = userRepository;
//        this.channelRepository = channelRepository;
//    }

    @Override
    public Message create(Message message) {
        validate(message);
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
        if (messageRepository.findById(message.getId()) == null) {
            throw new IllegalStateException("존재하지 않는 메세지입니다.");
        }
        validate(message);
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID id) {
        if (messageRepository.findById(id) == null) {
            throw new IllegalStateException("존재하지 않는 메세지입니다.");
        }
        messageRepository.delete(id);
    }

    private void validate(Message message) {
        if (userRepository.findById(message.getAuthorId()) == null) {
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }

        if (channelRepository.findById(message.getChannelId()) == null) {
            throw new IllegalStateException("존재하지 않는 채널입니다.");
        }
    }
}