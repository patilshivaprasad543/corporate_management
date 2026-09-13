package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "policy_rules")

public class PolicyRule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private TravelPolicy policy;

    @Column(name = "rule_type", length = 100)
    private String ruleType;

    @Column(name = "rule_key", length = 100)
    private String ruleKey;

    @Column(name = "rule_value", length = 255)
    private String ruleValue;

    @Column(name = "is_strict")

    private Boolean strict = false;


    public PolicyRule() {}

    public PolicyRule(TravelPolicy policy, String ruleType, String ruleKey, String ruleValue, Boolean strict) {
        this.policy = policy;
        this.ruleType = ruleType;
        this.ruleKey = ruleKey;
        this.ruleValue = ruleValue;
        this.strict = strict;
    }

    public TravelPolicy getPolicy() { return policy; }

    public void setPolicy(TravelPolicy policy) { this.policy = policy; }

    public String getRuleType() { return ruleType; }

    public void setRuleType(String ruleType) { this.ruleType = ruleType; }

    public String getRuleKey() { return ruleKey; }

    public void setRuleKey(String ruleKey) { this.ruleKey = ruleKey; }

    public String getRuleValue() { return ruleValue; }

    public void setRuleValue(String ruleValue) { this.ruleValue = ruleValue; }

    public Boolean isStrict() { return strict; }

    public Boolean getStrict() { return strict; }

    public void setStrict(Boolean strict) { this.strict = strict; }

    public static PolicyRuleBuilder builder() { return new PolicyRuleBuilder(); }

    public static class PolicyRuleBuilder {
        private Long id;
        private TravelPolicy policy;
        private String ruleType;
        private String ruleKey;
        private String ruleValue;
        private Boolean strict = false;

        public PolicyRuleBuilder id(Long id) { this.id = id; return this; }
        public PolicyRuleBuilder policy(TravelPolicy policy) { this.policy = policy; return this; }
        public PolicyRuleBuilder ruleType(String ruleType) { this.ruleType = ruleType; return this; }
        public PolicyRuleBuilder ruleKey(String ruleKey) { this.ruleKey = ruleKey; return this; }
        public PolicyRuleBuilder ruleValue(String ruleValue) { this.ruleValue = ruleValue; return this; }
        public PolicyRuleBuilder strict(Boolean strict) { this.strict = strict; return this; }

        public PolicyRule build() {
            PolicyRule obj = new PolicyRule();
            obj.setId(this.id);
            obj.setPolicy(this.policy);
            obj.setRuleType(this.ruleType);
            obj.setRuleKey(this.ruleKey);
            obj.setRuleValue(this.ruleValue);
            obj.setStrict(this.strict);
            return obj;
        }
    }
}
