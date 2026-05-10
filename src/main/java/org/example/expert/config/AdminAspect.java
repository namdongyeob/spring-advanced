package org.example.expert.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminAspect {

    private final ObjectMapper objectMapper;

    @Around("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || " +
            "execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public Object logAdminApi(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();

        Long userId = (Long) request.getAttribute("userId");
        String url = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();
        String requestBody = toJson(joinPoint.getArgs());

        log.info("[ADMIN API REQUEST] userId={}, requestTime={}, url={}, requestBody={}",
                userId, requestTime, url, requestBody);

        try {
            Object responseBody = joinPoint.proceed();

            log.info("[ADMIN API RESPONSE] userId={}, url={}, responseBody={}",
                    userId, url, toJson(responseBody));

            return responseBody;
        } catch (Exception e) {
            log.warn("[ADMIN API ERROR] userId={}, url={}, exception={}, message={}",
                    userId, url, e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return String.valueOf(value);
        }
    }

    private String toJson(Object[] values) {
        return toJson(Arrays.asList(values));
    }
}
