package com.julun.exceptions;

/**
 * 配置错误异常.
 */
@SuppressWarnings("serial")
public class ConfigException extends Exception {
    public ConfigException() {
        super();
    }

    public ConfigException(String message) {
        super(message);
    }
}
