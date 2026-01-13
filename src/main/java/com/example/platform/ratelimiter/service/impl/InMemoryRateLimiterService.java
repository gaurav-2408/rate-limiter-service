package com.example.platform.ratelimiter.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

import com.example.platform.ratelimiter.config.RateLimitProperties;
import com.example.platform.ratelimiter.model.RateLimitBucket;
import com.example.platform.ratelimiter.model.RateLimitRule;
import com.example.platform.ratelimiter.service.RateLimiterService;

@Service
public class InMemoryRateLimiterService implements RateLimiterService {

    private final Map<String, RateLimitBucket> buckets = new ConcurrentHashMap<>();
    private final Map<String, RateLimitRule> ruleMap = new ConcurrentHashMap<>();

    public InMemoryRateLimiterService(RateLimitProperties properties){
        for(RateLimitRule rule: properties.getRules()){
            String ruleKey = buildRuleKey(rule.getApi(), rule.getHttpMethod());
            ruleMap.put(ruleKey, rule);
        }
    }

    @Override
    public boolean isRequestAllowed(String clientId, String api, String httpMethod) {
        String ruleKey = buildRuleKey(api, httpMethod);
        RateLimitRule rule = ruleMap.get(ruleKey);

        if(null == rule){
            return true;
        }

        String bucketKey = buildBucketKey(clientId, api, httpMethod);

        RateLimitBucket bucket = buckets.computeIfAbsent(bucketKey, k->new RateLimitBucket(rule.getLimit(), rule.getWindowInSeconds()));

        return bucket.allowRequest();
    }

    private String buildBucketKey(String clientId, String api, String httpMethod) {
        return clientId + "|" + api + "|" + httpMethod;
    }

    private String buildRuleKey(String api, String method){
        return api + "|" + method;
    }
}
