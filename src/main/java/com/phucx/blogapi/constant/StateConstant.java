package com.phucx.blogapi.constant;

public enum StateConstant {
    PUBLIC(2),
    PRIVATE(1);
    private int value;
    private StateConstant(int value){
        this.value=value;
    }
    public int getValue(){
        return this.value;
    }
}
