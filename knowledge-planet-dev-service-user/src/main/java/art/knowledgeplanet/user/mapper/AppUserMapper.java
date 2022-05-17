package art.knowledgeplanet.user.mapper;

import art.knowledgeplanet.my.mapper.MyMapper;
import art.knowledgeplanet.pojo.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserMapper extends MyMapper<AppUser> {
}