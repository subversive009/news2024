package cn.edu.hbpu.news2022.config;

import java.util.Map;

import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;


@Component
@ConfigurationProperties(prefix = "jdbc")
@Data
@Validated
public class Prop {
	private String url;
	private String username;
	private String[] users;
	private Map pass;
	private String password;
	@Email(message = "邮箱格式不正确")
	private String email;
	//@Value("${upload.path}")
	//private String uploadPath;
	@Value("#{2*24}")
	private String result;

}
