package org.mian.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@RestController
@RequestMapping("/fakelms")
public class FakeLMSController {
    @PostMapping("/grade_passback")
    public Boolean gradePassback() {
        System.out.println("成绩回传成功");
        return true;
    }

    @PostMapping("/oauth2/token")
    public JSONObject getAccessToken(HttpServletRequest request) {
        // 从请求头中获取授权信息
        String authorization = request.getHeader("Authorization");
        System.out.println(authorization);

        // 从请求头中获取客户端凭据
        String[] credentials = authorization.split(" ");
        String base64Credentials = credentials[1];
        String clientCredentials = new String(Base64.getDecoder().decode(base64Credentials));
        String clientId = clientCredentials.split(":")[0];
        String clientSecret = clientCredentials.split(":")[1];

        JSONObject response = new JSONObject();
        // 检查客户端凭据
        if (!"your_client_id".equals(clientId) || !"your_client_secret".equals(clientSecret)) {
            System.out.println("客户端凭据错误");
            response.put("error", "invalid_client");
            response.put("error_description", "Client credentials are invalid");
            return response;
        }

        System.out.println("获取访问令牌成功");
        response.put("access_token", "有效的OAuth2访问令牌");
        response.put("token_type", "Bearer");
        response.put("expires_in", 3600);
        return response;
    }
}
