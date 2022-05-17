//package art.knowledgeplanet.api.controller.user.fallbacks;
//
//import art.knowledgeplanet.api.controller.user.UserControllerApi;
//import art.knowledgeplanet.grace.result.GraceJSONResult;
//import art.knowledgeplanet.grace.result.ResponseStatusEnum;
//import art.knowledgeplanet.pojo.bo.UpdateUserInfoBO;
//import art.knowledgeplanet.pojo.vo.AppUserVO;
//import feign.hystrix.FallbackFactory;
//import org.springframework.stereotype.Component;
//
//import javax.validation.Valid;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class UserControllerFactoryFallback
//        implements FallbackFactory<UserControllerApi> {
//
//    @Override
//    public UserControllerApi create(Throwable throwable) {
//        return new UserControllerApi() {
//            @Override
//            public GraceJSONResult getUserInfo(String userId) {
//                return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_FEIGN);
//            }
//
//            @Override
//            public GraceJSONResult getAccountInfo(String userId) {
//                return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_FEIGN);
//            }
//
//            @Override
//            public GraceJSONResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO) {
//                return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_FEIGN);
//            }
//
//            @Override
//            public GraceJSONResult queryByIds(String userIds) {
//                System.out.println("进入客户端（服务调用者）的降级方法");
//                List<AppUserVO> publisherList = new ArrayList<>();
//                return GraceJSONResult.ok(publisherList);
//            }
//        };
//    }
//}
