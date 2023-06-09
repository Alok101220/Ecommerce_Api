package com.alok91340.ecommerceapi.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// import jakarta.servlet.http.HttpServletRequest;

public class RequestClientIp {
    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    public static String getClientIpAddress(javax.servlet.http.HttpServletRequest request) {

        if (RequestContextHolder.getRequestAttributes() == null) {
            return "0.0.0.0";
        }

        request = (javax.servlet.http.HttpServletRequest) ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                String ip = ipList.split(",")[0];
                return ip;
            }
        }

        return request.getRemoteAddr();
    }
}
