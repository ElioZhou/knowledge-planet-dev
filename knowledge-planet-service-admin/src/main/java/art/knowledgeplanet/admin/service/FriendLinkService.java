package art.knowledgeplanet.admin.service;

import art.knowledgeplanet.pojo.AdminUser;
import art.knowledgeplanet.pojo.bo.NewAdminBO;
import art.knowledgeplanet.pojo.mo.FriendLinkMO;
import art.knowledgeplanet.utils.PagedGridResult;

import java.util.List;

public interface FriendLinkService {

    /**
     * 新增或者更新友情链接
     */
    public void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO);

    /**
     * 查询友情链接
     */
    public List<FriendLinkMO> queryAllFriendLinkList();

    /**
     * 删除友情链接
     */
    public void delete(String linkId);

    /**
     * 首页查询友情链接
     */
    public List<FriendLinkMO> queryPortalAllFriendLinkList();
}
