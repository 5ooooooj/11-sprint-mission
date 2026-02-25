package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.time.Instant;
import java.util.UUID;


public class JavaApplication {
    public static void main(String[] args) {
        // 유저 테스트
        // 구현체 변경 UserService userService = new JCFUserService();
        UserService userService = new FileUserService();
        System.out.println("========== 유저 테스트 ==========");

        User u1 = new User("강우진", "안녕하세요.");
        userService.create(u1); // 생성
        User u2 = new User("박지현", "반갑습니다.");
        userService.create(u2); // 생성
        User u3 = new User("한교동", "잘부탁드려요.");
        userService.create(u3); // 생성
        System.out.println("유저 ID : " + userService.findById(u1.getId())); // id 조회
        System.out.println("유저 ID : " + userService.findById(u2.getId())); // id 조회
        System.out.println("유저 ID : " + userService.findById(u3.getId())); // id 조회

        System.out.println("현재 유저 수 : " + userService.findAll().size() + "명"); // 전체 몇명 ?

        u1.updateUserName("강강우우진진"); // 이름 수정
        userService.update(u1);
        System.out.println("u1 이름 변경 후 : " + userService.findById(u1.getId()).getUserName());

        userService.delete(u1.getId()); // 삭제
        // userService.update(u1); 이미 삭제된게 맞아 그레서 update가 안돼
        // 그런데, 진짜 DB랑 연결한게 아니기때문에 값은 존재하는듯 ?

        System.out.println("현재 사용자 수 : " + userService.findAll().size() + "명");

        if (userService.findById(u1.getId()) == null) {
            System.out.println("존재하지 않는 id 입니다.");
        } else {
            System.out.println(userService.findById(u1.getId()));
        }

        // 채널 테스트
        // 구현체 교체 ChannelService channelService = new JCFChannelService();
        ChannelService channelService = new FileChannelService();
        System.out.println("========== 채널 테스트 ==========");

        Channel c1 = new Channel("모각코", "모여서각자코딩");
        channelService.create(c1);
        Channel c2 = new Channel("sb11", "스프링백엔드11기");
        channelService.create(c2);

        System.out.print("1번 채널명 : " + channelService.findById(c1.getId()).getChannelName());
        System.out.println(" // " + channelService.findById(c1.getId()).getDescription());
        System.out.print("2번 채널명 : " + channelService.findById(c2.getId()).getChannelName());
        System.out.println(" // " + channelService.findById(c2.getId()).getDescription());

        System.out.println("현재 채널 개수 : " + channelService.findAll().size() + "개");

        c2.updateChannelName("Code-it"); // 수정
        channelService.update(c2);
        System.out.println("바뀐 2번 채널명 : " + channelService.findById(c2.getId()).getChannelName()); // 조회

        channelService.delete(c1.getId());

        if (channelService.findById(c1.getId()) == null) {
            System.out.println("존재하지 않는 채널입니다.");
        } else {
            System.out.println(channelService.findById(c1.getId()));
        }

        // 메세지 테스트
        //MessageService messageService = new JCFMessageService(userService, channelService);
        MessageService messageService = new FileMessageService(userService, channelService);
        System.out.println("========== 메세지 테스트 ==========");

        User u = userService.create(new User("강우진", "Hi"));
        Channel c = channelService.create(new Channel("sb11", "스프링백엔드11기"));
        Message m = new Message(u.getId(), c.getId(), "반가워");

        messageService.create(m);
        System.out.println(u.getUserName() + "의 '" + c.getChannelName() + "' 채널에서 보낸 메세지 : " + m.getContent());

        u.updateUserName("강우진바보");
        m.updateContent("반가워바보 ~ ");
        userService.update(u);
        messageService.update(m);
        System.out.println(u.getUserName() + "의 '" + c.getChannelName() + "' 채널에서 보낸 메세지 : " + m.getContent());
        System.out.println("마지막 이름 변경 시각 : " + c.getUpdatedAtText());

        // 일치하는 user id가 없을때 검증
        try {
            Message bad = new Message(UUID.randomUUID(), c.getId(), "실패");
            messageService.create(bad);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // c1.getId는 위에서 삭제했음
        try {
            Message bad2 = new Message(u.getId(), c1.getId(), "실패");
            messageService.create(bad2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
