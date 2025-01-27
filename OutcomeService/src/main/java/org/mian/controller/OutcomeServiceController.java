package org.mian.controller;

import org.mian.dealer.OutcomeServiceDealer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/outcomeservice")
public class OutcomeServiceController {
    @PostMapping("/outcomes")
    public void handleOutcomes(@RequestParam String lmsOutcomeUrl, @RequestParam String accessToken, @RequestParam double score, @RequestParam String userId, @RequestParam String resourceLinkId) {
        OutcomeServiceDealer.sendGradeToLMS(lmsOutcomeUrl, accessToken, score, userId, resourceLinkId);
        System.out.println("成绩回传成功");
    }
}
