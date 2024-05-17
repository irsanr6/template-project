package com.irsan.templateproject.config.interceptor;

import com.irsan.templateproject.exception.InvalidApiKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 17/05/2024
 */
@Component
@Slf4j
public class AppsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("[preHandle][{}][{}][{}]", request, request.getMethod(), request.getRequestURI());
        String apiKey = request.getHeader("X-ApiKey");
        if (apiKey.equals("123456789")) {
            return true;
        } else {
            throw new InvalidApiKeyException("Invalid API Key");
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("[postHandle][{}]", request);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String message = "";
        if (ex != null) {
            message = ex.getMessage();
        }
        log.info("[afterCompletion][{}][{}]", request, message);
    }

}
