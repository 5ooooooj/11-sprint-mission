package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// UserRepository userRepository = new FileRepository("data/users.ser");
// UserService userService = new FileService(userRepository);
// 위 부분을 Spring에게 등록해주는 클래스 - AppConfig
// 즉, 객체 관리 및 필요한곳에 사용하게 해주는 부분
// 객체 생성과 의존성 연결을 main에서 하지 않고 Spring설정 클래스로 옮김

@Configuration // 조립할 코드만
public class AppConfig {

    @Bean
    public UserRepository userRepository() {
        return new FileUserRepository("data/users.ser");
    }

    @Bean
    public ChannelRepository channelRepository() {
        return new FileChannelRepository("data/channels.ser");
    }

    @Bean
    public MessageRepository messageRepository() {
        return new FileMessageRepository("data/messages.ser");
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new BasicUserService(userRepository);
    }

    @Bean
    public ChannelService channelService(ChannelRepository channelRepository) {
        return new BasicChannelService(channelRepository);
    }

    @Bean
    public MessageService messageService(
            MessageRepository messageRepository,
            UserRepository userRepository,
            ChannelRepository channelRepository
    ) {
        return new BasicMessageService(messageRepository, userRepository, channelRepository);
    }
}
