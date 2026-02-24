package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel create(Channel channel) {
        //    if (data.containsKey(channel.getId())){
        //        throw new IllegalStateException("이미 존재하는 id 입니다.");
        //    }
        data.put(channel.getId(), channel); // key(id)가 없으면 키 가져오고, value는 channel
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(Channel channel) {
        //    if(!data.containsKey(channel.getId())){
        //        throw new IllegalStateException("id가 존재하지 않습니다. id를 먼저 만드세요.");
        //    }
        data.put(channel.getId(), channel); // key(id)가 이미 있네? 덮어씀 + value 갱신
        return channel;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
