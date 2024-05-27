package cn.edu.hbpu.news2022.mapper;

import cn.edu.hbpu.news2022.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hbpu
 * @since 2022-03-09
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
	@Select("SELECT c.commentId,c.content,c.time,u.userName,u.image FROM `comment` c "
			+ "LEFT JOIN `user` u ON c.uid=u.uid WHERE c.newsId =#{newsId} ORDER BY c.commentId DESC")
	List<Comment> getByNewsId(Integer newsId);
}
