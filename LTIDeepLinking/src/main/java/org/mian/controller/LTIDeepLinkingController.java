package org.mian.controller;

import org.mian.dealer.LTIDeepLinkingDealer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LTIDeepLinkingController {
    // 1. 验证JWT（id_token）
    // 2. 生成LTI 1.3 内容项
    // 3. 生成签名后的JWT响应
    @PostMapping("/lti/deeplinking")
    public void handleLtiDeepLinking(@RequestParam String id_token, @RequestParam String deepLinkReturnUrl) {
        LTIDeepLinkingDealer.handleDeepLinkRequest(id_token, deepLinkReturnUrl);
    }
}
