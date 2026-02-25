package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private final File file;
    private Map<UUID, Channel> data;

    public FileChannelService() {
        this("data/channels.ser");
    }

    public FileChannelService(String filePath) {
        this.file = new File(filePath);
        this.data = load();
    }

    @Override
    public Channel create(Channel channel){
        data.put(channel.getId(),channel);
        save();
        return channel;
    }

    @Override
    public Channel findById(UUID id){
        return data.get(id);
    }

    @Override
    public List<Channel> findAll(){
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(Channel channel){
        data.put(channel.getId(),channel);
        save();
        return channel;
    }

    @Override
    public void delete(UUID id){
        data.remove(id);
        save();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID,Channel> load(){
        if (!file.exists() || file.length() == 0){
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            Object obj = ois.readObject();
            if (obj instanceof Map<?,?>){
                return (Map<UUID, Channel>) obj;
            }
            return new HashMap<>();
        }catch (EOFException e){
            return new HashMap<>();
        }catch (Exception e){
            throw new IllegalStateException("채널 데이터 로드 실패 : " + file.getAbsolutePath(),e);
        }
    }


    private void save() {
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new IllegalStateException("채널 데이터 저장 실패 : " + file.getAbsolutePath(), e);
        }
    }

}
