package com.na_ho.util.enumType;

public enum OptionType {
    PUT(-1), CALL(1);

    private final int value;
    private OptionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
