package art.knowledgeplanet.user.controller;

import art.knowledgeplanet.api.BaseController;
import art.knowledgeplanet.api.controller.user.AppUserMngControllerApi;
import art.knowledgeplanet.api.controller.user.HelloControllerApi;
import art.knowledgeplanet.enums.UserStatus;
import art.knowledgeplanet.grace.result.GraceJSONResult;
import art.knowledgeplanet.grace.result.ResponseStatusEnum;
import art.knowledgeplanet.pojo.AppUser;
import art.knowledgeplanet.pojo.vo.PublisherVO;
import art.knowledgeplanet.user.service.AppUserMngService;
import art.knowledgeplanet.user.service.UserService;
import art.knowledgeplanet.utils.JsonUtils;
import art.knowledgeplanet.utils.PagedGridResult;
import art.knowledgeplanet.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class AppUserMngController extends BaseController implements AppUserMngControllerApi {

    final static Logger logger = LoggerFactory.getLogger(AppUserMngController.class);

    @Autowired
    private AppUserMngService appUserMngService;

    @Autowired
    private UserService userService;

    @Override
    public GraceJSONResult queryAll(String nickname,
                                    Integer status,
                                    Date startDate,
                                    Date endDate,
                                    Integer page,
                                    Integer pageSize) {

//        System.out.println("nihao!!"+startDate);
//        System.out.println(endDate);
//        用DateConverterConfig做类型转换，String=>Date
        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult result = appUserMngService.queryAllUserList(nickname,
                                            status,
                                            startDate,
                                            endDate,
                                            page,
                                            pageSize);

        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult userDetail(String userId) {
        return GraceJSONResult.ok(userService.getUser(userId));
    }

    @Override
    public GraceJSONResult freezeUserOrNot(String userId, Integer doStatus) {
        if (!UserStatus.isUserStatusValid(doStatus)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }
        appUserMngService.freezeUserOrNot(userId, doStatus);

        // 刷新用户状态：
        // 1. 删除用户会话，从而保障用户需要重新登录以后再来刷新他的会话状态
        // 2. 查询最新用户的信息，重新放入redis中，做一次更新
        redis.del(REDIS_USER_INFO + ":" + userId);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryAll(String userIds) {
        if (StringUtils.isBlank(userIds)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }

        List<String> userIdList = JsonUtils.jsonToList(userIds, String.class);
//        System.out.println(userIdList);

        List<PublisherVO> userList = userService.getUserList(userIdList);

        return GraceJSONResult.ok(userList);
    }
}
