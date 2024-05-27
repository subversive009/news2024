package cn.edu.hbpu.news2022.mapper;

import cn.edu.hbpu.news2022.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
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
public interface NewsMapper extends BaseMapper<News> {
	@Select("Select n.*,k.content As kindName,u.userName,u.image FROM news n \r\n"
			+ "Left join kind k ON n.kindId=k.kindId \r\n"
			+ "Left join user u ON n.uid=u.uid WHERE newsId=#{newsid} ")
@Results({
	@Result(property = "picList",column = "newsid",
			many=@Many(select = "cn.edu.hbpu.news2022.mapper.PictureMapper.getPicListByNewsid")			)	
}			)	
	News getById(int newsid);
	
   @Select("SELECT n.newsId,n.title,n.video,n.pictures,n.time,n.from1,u.userName,u.image FROM news n "
    		+ "LEFT JOIN user u ON n.uid=u.uid WHERE n.kindid=#{kindid} AND n.type=3")
    List<News> getVideosByKindId(Integer kindId);
}
