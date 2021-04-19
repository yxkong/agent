package com.yxkong.agent.exeception;

/**
 * @Author: yxk
 * @Date: 2021/4/5 7:49 下午
 * @version: 1.0
 */
public class ParamsRuntimeExeception extends RuntimeException{
    public ParamsRuntimeExeception() {
    }

    public ParamsRuntimeExeception(String message) {
        super(message);
    }

    public ParamsRuntimeExeception(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamsRuntimeExeception(Throwable cause) {
        super(cause);
    }

    public ParamsRuntimeExeception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}