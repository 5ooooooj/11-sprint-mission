package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private final File file;
    private Map<UUID, Message> data;

    private final UserService userService;
    private final ChannelService channelService;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this(userService, channelService, "data/messages.ser");
    }

    public FileMessageService(UserService userService, ChannelService channelService,
                              String filePath) {
        this.userService = userService;
        this.channelService = channelService;
        this.file = new File(filePath);
        this.data = load();
    }

    @Override
    public Message create(Message message) {
        validate(message);
        data.put(message.getId(), message);
        save();
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
        validate(message);
        data.put(message.getId(), message);
        save();
        return message;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
        save();
    }

    // 검증 로직

    private void validate(Message message) {
        if (userService.findById(message.getUserId()) == null) {
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }
        if (channelService.findById(message.getChannelId()) == null) {
            throw new IllegalStateException("존재하지 않는 채널입니다.");
        }
    }

    // 파일 로드
    @SuppressWarnings("unchecked")
    private Map<UUID, Message> load() {
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<UUID, Message>) ois.readObject();
        }catch (Exception e){
            return new HashMap<>();
        }
    }

    private void save(){
        File parenet = file.getParentFile();
        if (parenet != null) parenet.mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            oos.writeObject(data);
        }catch (IOException e){
            throw new IllegalStateException("메세지 저장 실패 : ",e);
        }
    }
}
