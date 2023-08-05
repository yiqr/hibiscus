package org.yiqr.hibiscus.common.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author rose
 * @create 2023/7/19 23:16
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IPUtils {

    private static final String IP_UNKNOWN = "unknown";

    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String[] ips = StringUtils.split(ip, ",");
        if (ips.length != 1) {
            ip = ips[0];
        }
        return ip;
    }


    /**
     * 返回主机名的全限定域名
     */
    public static String getFullyLocalHostName() {
        try {
            InetAddress inet = InetAddress.getLocalHost();
            return inet.getCanonicalHostName();
        } catch (UnknownHostException e) {
            // ignore
        }
        return null;
    }

    /**
     * 返回本机IP
     */
    public static String getLocalHostAddress() {
        try {
            InetAddress inet = InetAddress.getLocalHost();
            return inet.getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    /**
     * 判断本机是否和传入的域名一致
     */
    public static boolean isDomainEqualsLocal(String domainName) {
        if (StringUtils.isBlank(domainName)) {
            return false;
        }
        try {
            return InetAddress.getByName(domainName).getHostAddress().equals(getLocalHostAddress());
        } catch (UnknownHostException e) {
            return false;
        }
    }
}