package cn.edu.hbpu.news2022.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mongodb.client.result.DeleteResult;

import cn.edu.hbpu.news2022.entity.FollowUser;
import cn.edu.hbpu.news2022.entity.User;
import cn.edu.hbpu.news2022.mapper.UserMapper;
import cn.edu.hbpu.news2022.service.FollowUserService;
@Service
public class FollowUserServiceImpl implements FollowUserService {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private RedisTemplate redisTemplate;
	
	private static final String FOLLOW_USER_KEY="FOLLOW_USER_KEY_";
	
	public boolean followUser(Long userId, Long followUserId) {
		FollowUser followUser = new FollowUser();
		followUser.setId(ObjectId.get());
		followUser.setUserId(userId);
		followUser.setFollowUserId(followUserId);
		followUser.setCreated(System.currentTimeMillis());
		mongoTemplate.save(followUser);
		redisTemplate.opsForHash().put(this.getFollowUserKey(userId),followUserId+"",1);
		return true;
	}

	private Object getFollowUserKey(Long userId) {
		return this.FOLLOW_USER_KEY+userId;
	}

	@Override
	public boolean disFollowUser(Long userId, Long followUserId) {
		Query query=Query.query(Criteria.where("userId").is(userId)
				.and("followUserId").is(followUserId));
		DeleteResult result=mongoTemplate.remove(query,FollowUser.class);
		if(result.getDeletedCount()>0) {
			redisTemplate.opsForHash().delete(this.getFollowUserKey(userId), followUserId+"");
			return true;
		}
		return false;
	}

	@Override
	public boolean isFollowUser(Long userId, Long followUserId) {
		// TODO Auto-generated method stub
		return redisTemplate.opsForHash().hasKey(this.getFollowUserKey(userId), followUserId+"");
	}
	@Autowired
	UserMapper userMapper;
	@Override
	public List<User> getFollowUsersByUid(Long userId) {
		//我关注的作者列表
		Query query=Query.query(Criteria.where("userId").is(userId));
		List<FollowUser> list=mongoTemplate.find(query, FollowUser.class);
		List<User> userList=new ArrayList<User>();
		for (FollowUser followUser : list) {
			QueryWrapper<User> wrapper=new QueryWrapper<>();
			wrapper.select("uid,username,image").eq("uid", followUser.getFollowUserId());
			User user=userMapper.selectOne(wrapper);
			userList.add(user);
		}
		return userList;
	}

	@Override
	public List<User> getUsersByFollowUid(Long followUserId) {
		//关注我的用户列表
		Query query=Query.query(Criteria.where("followUserId").is(followUserId));
		List<FollowUser> list=mongoTemplate.find(query, FollowUser.class);
		List<User> userList=new ArrayList<User>();
		for (FollowUser followUser : list) {
			QueryWrapper<User> wrapper=new QueryWrapper<>();
			wrapper.select("uid,username,image").eq("uid", followUser.getUserId());
			User user=userMapper.selectOne(wrapper);
			userList.add(user);
		}
		return userList;
	}

}
