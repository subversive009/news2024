package cn.edu.hbpu.news2022.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.edu.hbpu.news2022.entity.Chatmsg;
import cn.edu.hbpu.news2022.service.ChatmsgService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hbpu
 * @since 2022-03-30
 */
@RestController
@RequestMapping("/news/chatmsg")
public class ChatmsgController {
	@Autowired
	ChatmsgService chatmsgService;
	@GetMapping("/list")
	IPage<Chatmsg> list(Page<Chatmsg> page,Chatmsg chatmsg){
		QueryWrapper<Chatmsg> wrapper=new QueryWrapper<Chatmsg>();
		wrapper.eq("fromUser", chatmsg.getFromUser()).eq("toUser", chatmsg.getToUser())
		.or()
		.eq("fromUser", chatmsg.getToUser()).eq("toUser", chatmsg.getFromUser());
		return chatmsgService.page(page,wrapper);
	}

}

