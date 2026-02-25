package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private static final long serialVersionUID = 1L;
    private final File file;
    private Map<UUID, User> data;

    public FileUserService() {
        this("data/users.ser"); // 저장 파일명(원하면 변경 가능)
    }

    public FileUserService(String filePath) {
        this.file = new File(filePath);
        this.data = load();
    }

    @Override
    public User create(User user) {
        data.put(user.getId(), user);
        save();
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
        data.put(user.getId(), user);
        save();
        return user;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
        save();
    }

    // ---------- persistence ----------
    @SuppressWarnings("unchecked")
    private Map<UUID, User> load() {
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map<?, ?>) {
                return (Map<UUID, User>) obj;
            }
            return new HashMap<>();
        } catch (EOFException e) {
            return new HashMap<>();
        } catch (Exception e) {
            throw new IllegalStateException("유저 데이터 로드 실패: " + file.getAbsolutePath(), e);
        }
    }

    private void save() {
        // 폴더 경로를 쓸 수도 있으니, 부모 디렉토리 보장
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new IllegalStateException("유저 데이터 저장 실패: " + file.getAbsolutePath(), e);
        }
    }
}