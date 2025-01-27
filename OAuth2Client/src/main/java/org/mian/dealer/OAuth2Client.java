package org.mian.dealer;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class OAuth2Client {

    public static void main(String[] args) throws Exception {
        // Token URL 和凭据
        String tokenUrl = "http://localhost:9091/fakelms/oauth2/token";
        String clientId = "your_client_id";
        String clientSecret = "your_client_secret";
        String scope = "https://purl.imsglobal.org/spec/lti-ags/scope/score";

        // 获取访问令牌
        String accessToken = getAccessToken(tokenUrl, clientId, clientSecret, scope);
        System.out.println("Access Token: " + accessToken);
    }

    public static String getAccessToken(String tokenUrl, String clientId, String clientSecret, String scope) throws Exception {
        // 创建请求体
        String requestBody = "grant_type=client_credentials&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8.toString());

        // 创建请求头
        String authHeader = "Basic " + encodeBase64(clientId + ":" + clientSecret);

        // 创建URL对象
        URL url = new URL(tokenUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置请求方法和头信息
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", authHeader);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        // 发送请求
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 读取响应
        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        // 如果请求成功，解析响应
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 解析响应 JSON
            String responseString = response.toString();
            return parseAccessToken(responseString);
        } else {
            // 如果请求失败，抛出异常
            throw new IOException("Failed to get access token: " + response.toString());
        }
    }

    // 编码为 Base64
    private static String encodeBase64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    // 解析 JSON 响应中的 access_token
    private static String parseAccessToken(String response) {
        String[] parts = response.split(",");
        for (String part : parts) {
            if (part.contains("access_token")) {
                return part.split(":")[1].replace("\"", "").trim();
            }
        }
        return null;
    }
}

