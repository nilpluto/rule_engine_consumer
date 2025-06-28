package com.example.consumer.service;

import com.example.ruleengine.loader.RuleLoader;
import com.example.ruleengine.rules.DynamicExpressionRule;
import com.example.ruleengine.service.RuleEngineFacade;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RuleEvaluationService {

    public Map<String, Object> evaluate(Map<String, Object> inputFacts) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> facts = (Map<String, Object>) inputFacts.getOrDefault("facts", inputFacts);
            List<DynamicExpressionRule> rules = RuleLoader.getRules();

            // Reset evaluation state (if reused)
            for (DynamicExpressionRule rule : rules) rule.reset();

            RuleEngineFacade.evaluate(facts, rules);

            List<String> passedRules = new ArrayList<>();
            List<String> failedRules = new ArrayList<>();

            for (DynamicExpressionRule rule : rules) {
                String desc = "DynamicExpressionRule: Rule[" + rule.getName() + "]: " +
                        rule.getField() + " " + rule.getOperator() + " " + rule.getValue();
                if (rule.wasEvaluationPassed()) {
                    passedRules.add(desc);
                } else {
                    failedRules.add(desc);
                }
            }

            response.put("input", facts);
            response.put("passedRules", passedRules);
            response.put("failedRules", failedRules);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }
}
