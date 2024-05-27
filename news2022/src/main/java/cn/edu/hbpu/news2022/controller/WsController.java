package cn.edu.hbpu.news2022.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import cn.edu.hbpu.news2022.entity.Chatmsg;
import cn.edu.hbpu.news2022.service.ChatmsgService;

@Controller
public class WsController {
	@Autowired
	SimpMessagingTemplate messagingTemplate;
	@Autowired
	ChatmsgService chatmsgService;
	@MessageMapping("/chat")
	void chat(Chatmsg msg) {
		msg.setDate(LocalDateTime.now());
		chatmsgService.save(msg);
		messagingTemplate.convertAndSendToUser(msg.getToUser(), "/queue/chat", msg);
	}
}
