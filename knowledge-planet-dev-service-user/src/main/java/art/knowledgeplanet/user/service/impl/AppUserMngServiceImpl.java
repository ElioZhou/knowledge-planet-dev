package art.knowledgeplanet.user.service.impl;

import com.github.pagehelper.PageHelper;
import art.knowledgeplanet.api.service.BaseService;
import art.knowledgeplanet.enums.Sex;
import art.knowledgeplanet.enums.UserStatus;
import art.knowledgeplanet.exception.GraceException;
import art.knowledgeplanet.grace.result.ResponseStatusEnum;
import art.knowledgeplanet.pojo.AppUser;
import art.knowledgeplanet.pojo.bo.UpdateUserInfoBO;
import art.knowledgeplanet.user.mapper.AppUserMapper;
import art.knowledgeplanet.user.service.AppUserMngService;
import art.knowledgeplanet.user.service.UserService;
import art.knowledgeplanet.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class AppUserMngServiceImpl extends BaseService implements AppUserMngService {

    @Autowired
    public AppUserMapper appUserMapper;

    @Override
    public PagedGridResult queryAllUserList(String nickname,
                                            Integer status,
                                            Date startDate,
                                            Date endDate,
                                            Integer page,
                                            Integer pageSize) {

        Example example = new Example(AppUser.class);
        example.orderBy("createdTime").desc();
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(nickname)) {
            criteria.andLike("nickname", "%" + nickname + "%");
        }

        if (UserStatus.isUserStatusValid(status)) {
            criteria.andEqualTo("activeStatus", status);
        }

        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createdTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("createdTime", endDate);
        }

        PageHelper.startPage(page, pageSize);
        List<AppUser> list = appUserMapper.selectByExample(example);

        return setterPagedGrid(list, page);
    }

    @Transactional
    @Override
    public void freezeUserOrNot(String userId, Integer doStatus) {
        AppUser user = new AppUser();
        user.setId(userId);
        user.setActiveStatus(doStatus);
        appUserMapper.updateByPrimaryKeySelective(user);
    }
}
