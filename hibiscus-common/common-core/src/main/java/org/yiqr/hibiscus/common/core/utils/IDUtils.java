package org.yiqr.hibiscus.common.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author rose
 * @create 2023/7/19 23:24
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
