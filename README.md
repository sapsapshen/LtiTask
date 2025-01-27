# LTITasks
LTI 1p3 tasks

# LTIJWT => 题目1：LTI 1.3 启动请求的验证与处理
1. 运行test/JWTGenerator下main函数获取对应jwtToken；
2. 运行LTIJWT项目，启动接口监听（端口9080）；
3. 使用postman用POST方式，向localhost:9080/lti/launch，发送参数id_token，参数值填入第一步获取到的jwtToken；

# LTIDeepLinking => 题目2：LTI 1.3 深度链接（Deep Linking）响应生成
1. 运行LTIDeepLinking项目，启动接口监听（端口9081）；
2. 使用postman用POST方式，向localhost:9081/lti/deeplinking，发送参数id_token，（可用题目1中获取的jwtToken），以及参数deepLinkReturnUrl（LMS 提供的返回 URL）；

### FakeLMS => 桩代码，简单模拟LMS响应，为题目3、4测试用
1. 客户id（clientId）：your_client_id；
2. 客户口令（clientSecret）：your_client_secret；

# OutcomeService => 题目3：LTI 1.3 成绩回传（Outcome Service）实现
1. 启动FakeLMS（端口9091）；
2. 运行OutcomeService项目，启动接口监听（端口9082）；
3. 使用postman用POST方式，向localhost:9082/outcomeservice/outcomes，发送以下参数：lmsOutcomeUrl、accessToken、score、userId、resourceLinkId；

# OAuth2Client => 题目4：OAuth2 访问令牌获取
1. 启动FakeLMS（端口9091）；
2. 运行OAuth2Client项目，启动接口监听（端口9083）
3. 使用postman用POST方法，向localhost:9083/oauth2/token，发送以下参数：tokenUrl、clientId、clientSecret、scope；
