package art.knowledgeplanet.user.mapper;

import art.knowledgeplanet.my.mapper.MyMapper;
import art.knowledgeplanet.pojo.AppUser;
import art.knowledgeplanet.pojo.vo.PublisherVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AppUserMapperCustom {

    public List<PublisherVO> getUserList(Map<String, Object> map);

}