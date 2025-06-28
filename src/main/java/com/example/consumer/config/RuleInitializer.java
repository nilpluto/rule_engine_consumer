package com.example.consumer.config;


import com.example.ruleengine.loader.RuleLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RuleInitializer {

    @PostConstruct
    public void initRules() {
        try {
            // Load rules from classpath file: src/main/resources/rules.properties
            RuleLoader.loadFromClasspath("rules.properties");
            int count = RuleLoader.getRules().size();
            System.out.println("✅ Rules loaded successfully from classpath. Total rules: " + count);
        } catch (Exception e) {
            System.err.println("❌ Failed to load rules:");
            e.printStackTrace();
        }
    }
}