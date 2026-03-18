package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

// 예전 main에서는 객체생성, 의존성연결, 테스트실행 모두 담당
// AppConfig - 객체생성 및 연결
// TestDataRunner - 테스트
// DiscodeitApplication - 앱 시작
@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context
				= SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		userTest(userService);
		channelTest(channelService);
		messageTest(userService,channelService,messageService);
	}

	private static void userTest(UserService userService) {
		// 유저 테스트
		System.out.println("========== 유저 테스트 ==========");

		UserDto u1 = userService.create(new UserCreateRequest(
				"강우진",
				"dnwls@codeit.com",
				"1234",
				"안녕하세요.",
				null
		));

		UserDto u2 = userService.create(new UserCreateRequest(
				"박지현",
				"jh@codeit.com",
				"1111",
				"반갑습니다.",
				null
		));

		UserDto u3 = userService.create(new UserCreateRequest(
				"한교동",
				"han@codeit.com",
				"9999",
				"하이~",
				null
		));

		System.out.println("유저 ID : " +  userService.findById(u1.id()).id());
		System.out.println("유저 ID : " +  userService.findById(u2.id()).id());
		System.out.println("유저 ID : " +  userService.findById(u2.id()).id());


		System.out.println("유저 정보 : " + userService.findById(u1.id())); // 정보 조회
		System.out.println("유저 정보 : " + userService.findById(u2.id()));
		System.out.println("유저 정보 : " + userService.findById(u3.id()));

		System.out.println("현재 유저 수 : " + userService.findAll().size() + "명"); // 전체 몇명 ?

		userService.update(new UserUpdateRequest(
			u1.id(),
			"강강우우진진",
			u1.email(),
			null,
			u1.statusMessage(),
			null
		));
		System.out.println("u1 이름 변경 후 : " + userService.findById(u1.id()).userName());

		userService.delete(u1.id()); // 삭제
		// userService.update(u1); 이미 삭제된게 맞아 그레서 update가 안돼
		// 그런데, 진짜 DB랑 연결한게 아니기때문에 값은 존재하는듯 ?

		System.out.println("현재 사용자 수 : " + userService.findAll().size() + "명");

		try {
			System.out.println(userService.findById(u1.id()));
		} catch (Exception e){
			System.out.println("존재하지 않는 id 입니다.");
		}
	}

	private static void channelTest(ChannelService channelService) {
		// 채널 테스트
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
	}

	private static void messageTest(UserService userService,ChannelService channelService,MessageService messageService) {
		// 메세지 테스트
		System.out.println("========== 메세지 테스트 ==========");

		UserDto u = userService.create(new UserCreateRequest(
				"강우진",
				"dnwls@codeit.com",
				"1234",
				"Hi",
				null
		));
		Channel c = channelService.create(new Channel("sb11", "스프링백엔드11기"));
		Message m = new Message(u.id(), c.getId(), "반가워");

		messageService.create(m);
		System.out.println(u.userName() + "의 '" + c.getChannelName() + "' 채널에서 보낸 메세지 : " + m.getContent());

		userService.update(new UserUpdateRequest(
			u.id(),
			"강우진바보",
			u.email(),
			null,
			u.statusMessage(),
			null
		));
		m.updateContent("반가워바보 ~ ");
		messageService.update(m);

		UserDto updatedUser = userService.findById(u.id());
		System.out.println(updatedUser.userName() + "의 '" + c.getChannelName() + "' 채널에서 보낸 메세지 : " + m.getContent());
		System.out.println("마지막 이름 변경 시각 : " + updatedUser.updatedAt());
		System.out.println("마지막 메세지 변경 시각 : " + m.getUpdatedAtText());

		// 일치하는 user id가 없을때 검증
		try {
			Message bad = new Message(UUID.randomUUID(), c.getId(), "실패");
			messageService.create(bad);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// 존재하지 않는 channel 테스트
		Channel deletedChannel = channelService.create(new Channel("삭제용","테스트"));
		channelService.delete(deletedChannel.getId());

		try {
			Message bad2 = new Message(u.id(), deletedChannel.getId(), "실패");
			messageService.create(bad2);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
