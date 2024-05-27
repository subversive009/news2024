package cn.edu.hbpu.news2022.serviceImpl;

import cn.edu.hbpu.news2022.entity.News;
import cn.edu.hbpu.news2022.mapper.NewsMapper;
import cn.edu.hbpu.news2022.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hbpu
 * @since 2022-03-09
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
	@Autowired
	NewsMapper newsMapper;
	@Override
	public News getById(int newsid) {
		// TODO Auto-generated method stub
		return newsMapper.getById(newsid);
	}
	@Override
	public List<News> getVideosByKindId(Integer kindId) {
		// TODO Auto-generated method stub
		return newsMapper.getVideosByKindId(kindId);
	}

}
