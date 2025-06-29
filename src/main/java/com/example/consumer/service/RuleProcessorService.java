package com.example.consumer.service;

import com.example.ruleengine.loader.RuleLoader;
import com.example.ruleengine.rules.DynamicExpressionRule;
import com.example.ruleengine.service.RuleEngineFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service to evaluate rules and trigger user access control.
 */
@Service
public class RuleProcessorService {

    private final UserService userService;

    @Autowired
    public RuleProcessorService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Evaluates the rules against the given facts and calls access methods accordingly.
     *
     * @param facts input data for rule evaluation
     */
    public void evaluateAndProcess(Map<String, Object> facts) {
        List<DynamicExpressionRule> rules = RuleLoader.getRules();
        Map<String, Object> result = RuleEngineFacade.evaluate(facts, rules);

        @SuppressWarnings("unchecked")
        List<String> passedRules = (List<String>) result.getOrDefault("passedRules", List.of());

        boolean ageCheckPassed = passedRules.stream()
                .anyMatch(rule -> rule.contains("AgeCheck"));

        if (ageCheckPassed) {
            userService.allowAccess();
        } else {
            userService.denyAccess();
        }
    }
}
