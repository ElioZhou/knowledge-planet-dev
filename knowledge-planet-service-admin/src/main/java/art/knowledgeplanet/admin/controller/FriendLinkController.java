package art.knowledgeplanet.admin.controller;

import art.knowledgeplanet.admin.service.FriendLinkService;
import art.knowledgeplanet.api.BaseController;
import art.knowledgeplanet.api.controller.admin.FriendLinkControllerApi;
import art.knowledgeplanet.grace.result.GraceJSONResult;
import art.knowledgeplanet.pojo.bo.SaveFriendLinkBO;
import art.knowledgeplanet.pojo.mo.FriendLinkMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class FriendLinkController extends BaseController implements FriendLinkControllerApi {

    final static Logger logger = LoggerFactory.getLogger(FriendLinkController.class);

    @Autowired
    private FriendLinkService friendLinkService;

    @Override
    public GraceJSONResult saveOrUpdateFriendLink(
            @Valid SaveFriendLinkBO saveFriendLinkBO,
            BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return GraceJSONResult.errorMap(map);
        }

//        saveFriendLinkBO -> ***MO

        FriendLinkMO saveFriendLinkMO = new FriendLinkMO();
        BeanUtils.copyProperties(saveFriendLinkBO, saveFriendLinkMO);
        saveFriendLinkMO.setCreateTime(new Date());
        saveFriendLinkMO.setUpdateTime(new Date());

        friendLinkService.saveOrUpdateFriendLink(saveFriendLinkMO);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getFriendLinkList() {
        return GraceJSONResult.ok(friendLinkService.queryAllFriendLinkList());
    }

    @Override
    public GraceJSONResult delete(String linkId) {
        friendLinkService.delete(linkId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryPortalAllFriendLinkList() {
        List<FriendLinkMO> list = friendLinkService.queryPortalAllFriendLinkList();
        return GraceJSONResult.ok(list);
    }
}
