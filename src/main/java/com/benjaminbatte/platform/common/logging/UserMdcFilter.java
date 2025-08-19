// src/main/java/com/benjaminbatte/platform/common/logging/UserMdcFilter.java
package com.benjaminbatte.platform.common.logging;

import com.benjaminbatte.platform.common.security.CurrentUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserMdcFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String sub = null;
            try { sub = CurrentUser.externalIdOrThrow(); } catch (Exception ignored) {}
            if (sub != null) MDC.put("userId", sub);
            MDC.put("path", request.getRequestURI());
            chain.doFilter(request, response);
        } finally {
            MDC.remove("userId");
            MDC.remove("path");
        }
    }
}
