package cn.edu.hbpu.news2022.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyConfig {
	//@Bean
	public MyDataSource getDataSource() {
		return new MyDataSource();
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}