package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private final File file;
    private Map<UUID, Message> data;

    public FileMessageRepository() {
        this("data/messages.ser");
    }

    public FileMessageRepository(String filePath) {
        this.file = new File(filePath);
        this.data = load();
    }

    @Override
    public synchronized Message save(Message message) {
        data.put(message.getId(), message);
        persist();
        return message;
    }

    @Override
    public synchronized Message findById(UUID id) {
        return data.get(id);
    }

    @Override
    public synchronized List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public synchronized void delete(UUID id) {
        data.remove(id);
        persist();
    }

    // ---------- persistence ----------
    @SuppressWarnings("unchecked")
    private Map<UUID, Message> load() {
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map<?, ?>) {
                return (Map<UUID, Message>) obj;
            }
            return new HashMap<>();
        } catch (EOFException e) {
            return new HashMap<>();
        } catch (Exception e) {
            throw new IllegalStateException("메세지 데이터 로드 실패 : " + file.getAbsolutePath(), e);
        }
    }

    private void persist() {
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new IllegalStateException("메세지 데이터 저장 실패 : " + file.getAbsolutePath(), e);
        }
    }
}
