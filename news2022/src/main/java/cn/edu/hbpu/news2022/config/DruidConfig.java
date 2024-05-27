package cn.edu.hbpu.news2022.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DruidConfig {
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource getDataSource() {
		return new DruidDataSource();
	}
}
