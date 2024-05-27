package cn.edu.hbpu.news2022.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.hbpu.news2022.config.MyProperties;
import cn.edu.hbpu.news2022.config.Prop;

@RestController
public class HelloController {
	@RequestMapping("/hello")
	//@GetMapping("/hello")
	public String hello() {
		
		return "HBPU 湖北理工学院计算机学院";
	}
	
	
	@RequestMapping("/reputation")
	public String reputation() {
		return "in the death of her past, She felt truly alive";
	}
	
	
	@Autowired
	Prop prop;
	@RequestMapping("/Prop")
	public String prop() {
		
		return prop.toString();
	}
	@Autowired
	MyProperties myproperties;
	@RequestMapping("/myproperties")
	public String myproperties() {
		
		return myproperties.toString();
	}
	
	
	
}
