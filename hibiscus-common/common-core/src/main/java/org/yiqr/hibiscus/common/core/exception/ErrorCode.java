package org.yiqr.hibiscus.common.core.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author rose
 * @create 2023/7/14 18:23
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCode {
    public static final int SUCCESSFUL = 0;    // 请求成功
    public static final int FAIL = -1; // 失败

    public static final int API_ARGUMENT_INVALID = -100;    // API请求参数校验不通过（如: NotBlank NotEmpty）
    public static final int API_VIOLATION_ERROR = -101;     // 实体或方法参数约束校验不通过



}
