package art.knowledgeplanet.admin.controller;

import art.knowledgeplanet.api.controller.user.HelloControllerApi;
import art.knowledgeplanet.grace.result.GraceJSONResult;
import art.knowledgeplanet.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    public Object hello() {
        return GraceJSONResult.ok();
    }

}
