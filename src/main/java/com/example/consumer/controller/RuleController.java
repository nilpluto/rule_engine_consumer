package com.example.consumer.controller;

import com.example.ruleengine.loader.RuleLoader;
import com.example.ruleengine.rules.DynamicExpressionRule;
import com.example.ruleengine.service.RuleEngineFacade;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class RuleController {

    @PostMapping("/evaluate")
    public Map<String, Object> evaluateRules(@RequestBody Map<String, Object> inputFacts) {
        try {
            List<DynamicExpressionRule> rules = RuleLoader.getRules();  // ✅ static cache
            return RuleEngineFacade.evaluate(inputFacts.get("facts") instanceof Map ?
                    (Map<String, Object>) inputFacts.get("facts") : inputFacts, rules);
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }
}
