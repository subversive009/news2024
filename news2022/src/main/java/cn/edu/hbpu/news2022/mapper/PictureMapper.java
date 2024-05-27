package cn.edu.hbpu.news2022.mapper;

import cn.edu.hbpu.news2022.entity.Picture;
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
 * @since 2022-03-17
 */
@Mapper
public interface PictureMapper extends BaseMapper<Picture> {
	@Select("select pid,pic from picture where newsid=#{newsid}")
	public List<Picture> getPicListByNewsid(int newsid);
}
