package org.yiqr.hibiscus.common.core.exception;

import lombok.Getter;

/**
 * @author rose
 * @create 2023/7/14 18:25
 */
@Getter
public class RException extends RuntimeException {
    private final int code;
    private String msg;

    public RException(int code) {
        this.code = code;
    }

    public RException(int code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public RException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public RException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.msg = message;
    }

    public static RException error(String errorMsg) {
        return new RException(ErrorCode.FAIL, errorMsg);
    }

    public static RException error(int errorCode, String errorMsg) {
        return new RException(errorCode, errorMsg);
    }

    public static RException error(int errorCode, String errorMsg, Throwable cause) {
        return new RException(errorCode, errorMsg, cause);
    }

    public static RException of(int errorCode) {
        return new RException(errorCode);
    }

    public static RException of(int errorCode, String errorMsg) {
        return new RException(errorCode, errorMsg);
    }

    public static RException of(int errorCode, String errorMsg, Throwable cause) {
        return new RException(errorCode, errorMsg, cause);
    }
}
