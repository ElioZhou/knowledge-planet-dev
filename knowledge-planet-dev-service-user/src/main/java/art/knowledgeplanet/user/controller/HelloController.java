package art.knowledgeplanet.user.controller;

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
    @Autowired
    private RedisOperator redis;
    public Object hello() {
        logger.debug("debug: hello~");
        logger.info("info: hello~");
        logger.warn("warn: hello~");
        logger.error("error: hello~");
        return GraceJSONResult.ok();
    }
    @GetMapping("/redis")
    public GraceJSONResult redisTest() {
        redis.set("name", "yuzhen");
        return GraceJSONResult.ok(redis.get("name"));
    }
}
