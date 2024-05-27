package cn.edu.hbpu.news2022.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@PropertySource("classpath:test.properties")
@ConfigurationProperties(prefix = "info")
@Data


public class MyProperties {
	private String username;
	private String password;
	private String uid;
	private int val;
	private String nickname;

}
