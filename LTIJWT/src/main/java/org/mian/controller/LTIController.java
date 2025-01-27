package org.mian.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.bind.annotation.*;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.HashMap;
import java.util.Map;

import static org.mian.utils.JWTTokenUtil.SECRET;

@RestController
@RequestMapping("/lti")
public class LTIController {
    @PostMapping("/launch")
    public Map<String, Object> handleLtiLaunch(@RequestParam String id_token) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 解析和验证 JWT
            // 创建一个验证的对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            DecodedJWT verify = jwtVerifier.verify(id_token);

            // 检查 LTI 版本和消息类型
            String ltiVersion = verify.getClaim("lti_version").asString();
            String ltiMessageType = verify.getClaim("lti_message_type").asString();

            if (ltiVersion == null || !ltiVersion.equals("LTI-1p3") || ltiMessageType == null) {
                response.put("error", "Invalid LTI version or message type");
                return response;
            }

            // 提取用户信息、课程信息、角色和资源链接ID
            String userId = verify.getClaim("sub").asString();
            String userName = verify.getClaim("name").asString();
            String email = verify.getClaim("email").asString();
            String roles = verify.getClaim("roles").asString();
            String courseId = verify.getClaim("context_id").asString();
            String courseLabel = verify.getClaim("context_label").asString();
            String courseTitle = verify.getClaim("context_title").asString();
            String resourceLinkId = verify.getClaim("resource_link_id").asString();

            // 构建响应
            Map<String, Object> user = new HashMap<>();
            user.put("id", userId);
            user.put("name", userName);
            user.put("email", email);
            user.put("roles", roles != null ? roles.split(",") : new String[]{});

            Map<String, Object> course = new HashMap<>();
            course.put("id", courseId);
            course.put("label", courseLabel);
            course.put("title", courseTitle);

            response.put("status", "success");
            response.put("user", user);
            response.put("course", course);
            response.put("resource_link_id", resourceLinkId);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Invalid JWT: signature verification failed");
        }

        return response;
    }
}

