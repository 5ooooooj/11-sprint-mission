package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);

    Channel findById(UUID id);

    List<Channel> findAll();

//    Channel update(Channel channel);
//    >> 레포지토리는 save 하나로 저장,업데이트 다 할수 있으니까

    void delete(UUID id);
}
