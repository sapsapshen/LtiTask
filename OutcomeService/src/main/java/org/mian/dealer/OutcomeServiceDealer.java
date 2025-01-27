package org.mian.dealer;
import com.alibaba.fastjson.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

public class OutcomeServiceDealer {

    public static void main(String[] args) {
        // 示例输入数据
        String lmsOutcomeUrl = "http://localhost:9091/fakelms/grade_passback";
        String accessToken = "有效的OAuth2访问令牌";  // 请替换为实际的OAuth2令牌
        double score = 0.95;
        String userId = "user-123";
        String resourceLinkId = "resource-link-uid";

        // 调用成绩回传功能
        sendGradeToLMS(lmsOutcomeUrl, accessToken, score, userId, resourceLinkId);
    }

    public static void sendGradeToLMS(String lmsOutcomeUrl, String accessToken, double score, String userId, String resourceLinkId) {
        try {
            // 创建 URL 对象
            URL url = new URL(lmsOutcomeUrl);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // 构建 JSON 请求体
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("user_id", userId);
            jsonInput.put("resource_link_id", resourceLinkId);
            jsonInput.put("score", score);

            // 发送请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 获取响应
            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 处理响应
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 成功响应
                System.out.println("Grade passback succeeded for user " + userId + " with score " + score + ".");
            } else {
                // 失败响应
                JSONObject jsonResponse = new JSONObject();
                String errorMessage = "Failed to pass back grade: <error details>";
                jsonResponse.put("error", errorMessage);
                System.out.println(jsonResponse.toJSONString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

