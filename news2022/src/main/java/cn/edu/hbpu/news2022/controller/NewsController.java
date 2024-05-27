package cn.edu.hbpu.news2022.controller;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import cn.edu.hbpu.news2022.util.GPT3Util;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.json.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.edu.hbpu.news2022.entity.News;
import cn.edu.hbpu.news2022.entity.QueryVo;
import cn.edu.hbpu.news2022.service.NewsService;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.UUID;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hbpu
 * @since 2022-03-09
 */
@RestController
@RequestMapping("/news")
public class NewsController {

	@Autowired
	private NewsService newsService;


	@PostMapping("/generateWithGPT3")
	public String generateWithGPT3(@RequestParam int newsId) {
		// 1. 从数据库中获取news的内容
		News news = newsService.getById(newsId);
		String content = news.getContent() + "\n总结一下这段文字的内容";

		// 2. 创建一个新的JSONObject
		JSONObject jsonObject = new JSONObject();

		// 3. 将content添加到JSONObject
		jsonObject.put("content", content);

		// 4. 将JSONObject转换为字符串
		String jsonContent = jsonObject.toString();

		System.out.println(jsonContent);

		// 5. 使用GPT3Util.sendPost方法生成内容
		String generatedContent = "";
		try {
			generatedContent = GPT3Util.sendPost(jsonContent);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// 6. 返回生成的内容
		return generatedContent;
	}

	@GetMapping("/test")
	public String test(){
		return GPT3Util.sendPost("你好");
	}

	@GetMapping("/getSwipeNews")
	public IPage<News> getSwipe(){
		Page <News> page=new Page<>(1,5);
		QueryWrapper<News> queryWrapper=new QueryWrapper<News>();
		queryWrapper.select("newsId","pictures").orderByAsc("newsId");
		return newsService.page(page,queryWrapper);
	}

	@GetMapping("/getPageNews")
	public IPage<News> getPage(Page<News> page){

		QueryWrapper<News> queryWrapper=new QueryWrapper<News>();
		queryWrapper.select("newsId","pictures","title","time","from1").orderByDesc("newsId");
		return newsService.page(page,queryWrapper);
	}

	@GetMapping("/getByUid")
	public IPage<News> getByUid(Page<News> page,Integer uid){

		QueryWrapper<News> queryWrapper=new QueryWrapper<News>();
		queryWrapper.select("newsId","pictures","title","time","from1")
				.eq("uid", uid)
				.orderByDesc("newsId");
		return newsService.page(page,queryWrapper);
	}

	@Value("${web.uploadPath}")
	String uploadPath;

	@PostMapping("/add")
	String add(News news,MultipartFile file) {
		if(file.isEmpty()) {
			return "failed";
		}
		String fileName=file.getOriginalFilename();

		String newFileName=UUID.fastUUID().toString(true)
				+"."+FileNameUtil.extName(fileName);
		File filePath=new File(uploadPath,newFileName);
		try {
			file.transferTo(filePath);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		news.setPictures(newFileName);
		news.setTime(LocalDateTime.now());
		newsService.save(news);
		return "success";
	}

	@GetMapping("/getById")
	public News getById(int newsId) {
		return newsService.getById(newsId);
	}

	@GetMapping("/download")
	public byte[] download(String filename) {
		File file=new File(uploadPath+filename);
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		try {
			return FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage().getBytes();
		}
	}

	@GetMapping("/getByKindId")
	List<News> getByKindId(int kindId){
		QueryWrapper<News> queryWrapper=new QueryWrapper<News>();
		queryWrapper.select("newsId","pictures","title","time","from1")
				.eq("kindId",kindId ).orderByDesc("newsId");
		return newsService.list(queryWrapper);

	}

	@GetMapping("/getVideosByKindId")
	public List<News> getVideosByKindId(int KindId){
		return newsService.getVideosByKindId(KindId);
	}

	@PostMapping("/getPageNewsByTitle")
	IPage<News> getPageNewsByTitle(@RequestBody QueryVo vo){
		Page<News> page = new Page<News>(vo.getCurrent(),10);
		QueryWrapper<News> queryWrapper = new QueryWrapper<News>();
		queryWrapper.select("newsId","pictures","title","time","from1")
				.like("title", vo.getTitle()).orderByDesc("newsId");
		return newsService.page(page,queryWrapper);
	}

}

