package com.bicycle.enums;

public enum RandomPrefix {

    BICYCLE_PREFIX("BY"), // 药品基本信息id前缀
    ;

    private String drugPrefix;

    RandomPrefix(String drugPrefix) {
        this.drugPrefix = drugPrefix;
    }

    public String getDrugPrefix() {
        return drugPrefix;
    }
}

















































