package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // 생성 읽기 모두읽기 수정 삭제 - 순서
    User create(User user);

    User findById(UUID id);

    List<User> findAll();

    User update(User user);

    void delete(UUID id);
}
