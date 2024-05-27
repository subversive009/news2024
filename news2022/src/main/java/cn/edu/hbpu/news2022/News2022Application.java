package cn.edu.hbpu.news2022;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
@SpringBootApplication

@MapperScan("cn.edu.hbpu.news2022.mapper")
public class News2022Application {

	public static void main(String[] args) {
		SpringApplication.run(News2022Application.class, args);
	}

}
