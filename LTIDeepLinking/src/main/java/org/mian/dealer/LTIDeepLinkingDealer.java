package org.mian.dealer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LTIDeepLinkingDealer {
    public static final String SECRET = "ASD!@#F^%A";

    // 1. 验证JWT（id_token）
    public static DecodedJWT validateIdToken(String id_token) {
        // 解析和验证 JWT
        // 创建一个验证的对象
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT verify = jwtVerifier.verify(id_token);
        return verify;
    }

    // 2. 生成LTI 1.3 内容项
    public static Map<String, Object> generateLtiContentItem() {
        Map<String, Object> contentItem = new HashMap<>();
        contentItem.put("type", "ltiResourceLink");
        contentItem.put("title", "Sample Content");
        contentItem.put("url", "https://tool.example.com/resource/123");
        contentItem.put("text", "A sample learning resource");

        // 图标信息
        Map<String, Object> icon = new HashMap<>();
        icon.put("width", 48);
        icon.put("height", 48);
        icon.put("url", "https://tool.example.com/icons/resource.png");

        contentItem.put("icon", icon);

        Map<String, Object> response = new HashMap<>();
        response.put("content_items", Collections.singletonList(contentItem));
        response.put("data", "some-state-data");

        return response;
    }

    // 3. 生成签名后的JWT响应
    public static String generateSignedJwt(Map<String, Object> payload) {
        String jwtToken = com.auth0.jwt.JWT.create()
                // 第一部分Header
//                .withHeader(headers)
                // 第二部分Payload
                .withPayload(payload)
                // 第三部分Signature
                .sign(Algorithm.HMAC256(SECRET));
        return jwtToken;
    }

    // 4. 发送请求到 LMS 的 deep_link_return_url
    public static void sendResponseToLms(String deepLinkReturnUrl, String signedJwt) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(deepLinkReturnUrl);
        post.setEntity(new StringEntity(signedJwt, StandardCharsets.UTF_8));
        post.setHeader("Content-Type", "application/json");

        client.execute(post);
    }

    // 主函数：处理 LTI 1.3 深度链接请求
    public static void handleDeepLinkRequest(String id_token, String deepLinkReturnUrl) {
        try {
            // 1. 验证 JWT
            DecodedJWT claims = validateIdToken(id_token);

            // 2. 生成 LTI 1.3 内容项
            Map<String, Object> payload = generateLtiContentItem();

            // 3. 生成签名后的 JWT
            String signedJwt = generateSignedJwt(payload);

            // 4. 发送响应到 LMS
            sendResponseToLms(deepLinkReturnUrl, signedJwt);

            System.out.println("LTI Deep Link response sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 主程序入口（用于测试）
    public static void main(String[] args) {
        String id_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsdGlfdmVyc2lvbiI6IkxUSS0xcDMiLCJsdGlfbWVzc2FnZV90eXBlIjoiTFRJLUxhdW5jaCIsImNvbnRleHRfdGl0bGUiOiJJbnRyb2R1Y3Rpb24gdG8gQ29tcHV0ZXIgU2NpZW5jZSIsInJvbGVzIjoiTGVhcm5lciIsIm5hbWUiOiJBbGljZSBTbWl0aCIsImNvbnRleHRfaWQiOiJjb3Vyc2UtNDU2IiwicmVzb3VyY2VfbGlua19pZCI6InJlc291cmNlLWxpbmstdWlkIiwiZXhwIjoxNzM3OTY3NjI5LCJjb250ZXh0X2xhYmVsIjoiQ1MxMDEiLCJlbWFpbCI6ImFsaWNlQGV4YW1wbGUuY29tIn0.Ef73K0_R7mhrhpik4LEJYJfuwYyE_CjSOvxitl_yOyU";
        String deepLinkReturnUrl = "https://lms.example.com/deep_link_return";

        handleDeepLinkRequest(id_token, deepLinkReturnUrl);
    }
}

