package art.knowledgeplanet.admin.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PWDTest {

    public static void main(String[] args) {
        // 密码加密，加盐
        String pwd = BCrypt.hashpw("admin", BCrypt.gensalt());
        System.out.println(pwd);
    }

}
