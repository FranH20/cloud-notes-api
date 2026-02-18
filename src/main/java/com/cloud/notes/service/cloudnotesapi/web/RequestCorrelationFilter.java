package com.cloud.notes.service.cloudnotesapi.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class RequestCorrelationFilter extends OncePerRequestFilter {

    public static final String REQUEST_ID_HEADER = "X-Request-Id";
    public static final String MDC_REQUEST_ID_KEY = "requestId";

    private static final Logger log = LoggerFactory.getLogger(RequestCorrelationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestId = Optional.ofNullable(request.getHeader(REQUEST_ID_HEADER))
                .filter(v -> !v.isBlank())
                .orElse(UUID.randomUUID().toString());

        long startNs = System.nanoTime();

        MDC.put(MDC_REQUEST_ID_KEY, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);

        String method = request.getMethod();
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String target = query == null ? path : (path + "?" + query);

        String userAgent = Optional.ofNullable(request.getHeader("User-Agent")).orElse("-");

        log.info("request.start method={} path={} userAgent=\"{}\"", method, target, userAgent);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = (System.nanoTime() - startNs) / 1_000_000;
            int status = response.getStatus();
            log.info("request.end method={} path={} status={} durationMs={}", method, target, status, durationMs);
            MDC.remove(MDC_REQUEST_ID_KEY);
        }
    }
}

