package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User create(User user) {
    //    if (data.containsKey(user.getId())) {
    //        throw new IllegalStateException("id가 존재하지 않습니다. id를 먼저 만드세요.");
    //    }
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(User user) {
    //    if (!data.containsKey(user.getId())) {
    //        throw new IllegalStateException("id가 존재하지 않습니다. id를 먼저 만드세요.");
    //    }
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
