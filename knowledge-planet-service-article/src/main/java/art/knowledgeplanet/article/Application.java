package art.knowledgeplanet.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "art.knowledgeplanet.article.mapper")
@ComponentScan(basePackages = {"art.knowledgeplanet", "org.n3r.idworker"})
@EnableEurekaClient
@EnableFeignClients({"art.knowledgeplanet"})
//@EnableHystrix
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
