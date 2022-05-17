package art.knowledgeplanet.admin.service;

import art.knowledgeplanet.pojo.AdminUser;
import art.knowledgeplanet.pojo.bo.NewAdminBO;
import art.knowledgeplanet.utils.PagedGridResult;

public interface AdminUserService {

    /**
     * 获得管理员的用户信息
     */
    public AdminUser queryAdminByUsername(String username);

    /**
     * 新增管理员
     */
    public void createAdminUser(NewAdminBO newAdminBO);

    /**
     * 分页查询admin列表
     */
    public PagedGridResult queryAdminList(Integer page, Integer pageSize);

}
