package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);

    Message findById(UUID id);

    List<Message> findAll();

//    Message update(Message message);
// >> 레포지토리는 save 하나로 저장,업데이트 다 할수 있으니까

    void delete(UUID id);
}
