package cn.edu.hbpu.news2022.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.edu.hbpu.news2022.entity.Comment;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hbpu
 * @since 2022-03-09
 */
public interface CommentService extends IService<Comment> {
	List<Comment> getByNewsId(Integer newsId);

}
