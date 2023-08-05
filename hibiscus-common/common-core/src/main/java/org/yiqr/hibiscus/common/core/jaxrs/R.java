package org.yiqr.hibiscus.common.core.jaxrs;

import lombok.Getter;
import org.yiqr.hibiscus.common.core.exception.ErrorCode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author rose
 * @create 2023/7/14 18:21
 */
@Getter
public class R extends HashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "message";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    public R() {
    }

    public R(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    public R(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (Objects.nonNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public static R success() {
        return R.success("operation successful");
    }

    public static R success(Object data) {
        return R.success("operation successful", data);
    }

    public static R success(String msg) {
        return R.success(msg, null);
    }

    public static R success(String msg, Object data) {
        return new R(ErrorCode.SUCCESSFUL, msg, data);
    }

    public static R error() {
        return R.error("operation failed");
    }

    public static R error(String msg) {
        return R.error(msg, null);
    }

    public static R error(String msg, Object data) {
        return new R(ErrorCode.FAIL, msg, data);
    }

    public static R error(int code, String msg) {
        return new R(code, msg, null);
    }

    public static R of(int code, String msg) {
        return new R(code, msg, null);
    }

    public static R of(int code, String msg, Object data) {
        return new R(code, msg, data);
    }
}
