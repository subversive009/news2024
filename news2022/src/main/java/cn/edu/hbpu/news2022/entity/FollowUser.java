package cn.edu.hbpu.news2022.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("followUser")
public class FollowUser {
	private ObjectId id;
	private Long userId;//登录用户Id
	private Long followUserId;//关注的作者Id
	private Long created;
}
