package org.mian.controller;

import org.mian.dealer.OAuth2Client;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    @PostMapping("/token")
    public String getToken(@RequestParam String tokenUrl, @RequestParam String clientId, @RequestParam String clientSecret, @RequestParam String scope) throws Exception {
        return OAuth2Client.getAccessToken(tokenUrl, clientId, clientSecret, scope);
    }
}
