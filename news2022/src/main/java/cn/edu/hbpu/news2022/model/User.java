package cn.edu.hbpu.news2022.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class User {
	@TableId
	private Integer uid;
	@TableField("userName")
	private String userName;
	private String password;
	private String image;
	@TableField("regTime")
	private Date regTime;
	private String email;
	private int type;
}
