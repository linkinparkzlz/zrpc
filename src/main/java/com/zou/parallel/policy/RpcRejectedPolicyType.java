package com.zou.parallel.policy;

/**
 * 拒绝策略类型
 */
public enum RpcRejectedPolicyType {


    ABORT_POLICY("AbortPolcy"),
    BLOCKING_POLICY("BlockingPolicy"),
    CALLER_RUNS_POLICY("RpcCallerRunPolicy"),
    DISCARDED_POLICY("RpcDiscardedPolicy"),
    REJECTED_POLICY("RejectedPolicy");


    private String value;

    private RpcRejectedPolicyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RpcRejectedPolicyType fromString(String value) {

        for (RpcRejectedPolicyType type : RpcRejectedPolicyType.values()) {

            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Mismatched type:" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}


























































