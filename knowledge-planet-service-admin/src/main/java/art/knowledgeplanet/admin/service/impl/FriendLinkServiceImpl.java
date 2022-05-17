package art.knowledgeplanet.admin.service.impl;

import art.knowledgeplanet.admin.repository.FriendLinkRepository;
import art.knowledgeplanet.admin.service.FriendLinkService;
import art.knowledgeplanet.enums.YesOrNo;
import art.knowledgeplanet.pojo.mo.FriendLinkMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendLinkServiceImpl implements FriendLinkService {

    @Autowired
    private FriendLinkRepository friendLinkRepository;

    @Override
    public void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO) {
        friendLinkRepository.save(friendLinkMO);
    }

    @Override
    public List<FriendLinkMO> queryAllFriendLinkList() {
        return friendLinkRepository.findAll();
    }

    @Override
    public void delete(String linkId) {
        friendLinkRepository.deleteById(linkId);
    }

    @Override
    public List<FriendLinkMO> queryPortalAllFriendLinkList() {
        return friendLinkRepository.getAllByIsDelete(YesOrNo.NO.type);
    }
}
