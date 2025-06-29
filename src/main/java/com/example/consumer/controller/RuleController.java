package com.example.consumer.controller;

import com.example.consumer.service.RuleProcessorService;
import com.example.ruleengine.loader.RuleLoader;
import com.example.ruleengine.rules.DynamicExpressionRule;
import com.example.ruleengine.service.RuleEngineFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class RuleController {

    @Autowired
    private RuleProcessorService ruleService;

    /**
     * Endpoint to evaluate rules against input facts.
     * The input should be a JSON object with a "facts" key containing the data to evaluate.
     * Example input:
     * curl --location 'http://localhost:8081/api/evaluate' \
     * --header 'Content-Type: application/json' \
     * --data '{
     *   "age": 15,
     *   "country": "India",
     *   "price": 80
     * }'
     *
     * @param inputFacts Map containing the facts to evaluate
     * @return Map with evaluation results
     */
    @PostMapping("/evaluate")
    public Map<String, Object> evaluateRules(@RequestBody Map<String, Object> input) {
        try {
            List<DynamicExpressionRule> rules = RuleLoader.getRules(); // ✅ loaded from cache
            Map<String, Object> facts = extractFacts(input);
            return RuleEngineFacade.evaluate(facts, rules);
        } catch (Exception e) {
            return Map.of("error", "Evaluation failed: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractFacts(Map<String, Object> input) {
        Object facts = input.get("facts");
        return (facts instanceof Map) ? (Map<String, Object>) facts : input;
    }

    @PostMapping("/check-access")
    public String checkAccessAndAct(@RequestBody Map<String, Object> inputFacts) {
        Map<String, Object> facts = (Map<String, Object>) inputFacts.getOrDefault("facts", inputFacts);
        ruleService.evaluateAndProcess(facts);
        return "Access decision processed. Check logs for result.";
    }
}
