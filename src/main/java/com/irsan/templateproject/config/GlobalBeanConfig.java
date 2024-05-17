package com.irsan.templateproject.config;

import com.irsan.templateproject.config.interceptor.AppsInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.MappedInterceptor;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 17/05/2024
 */
@Configuration
public class GlobalBeanConfig {

    @Bean
    public DispatcherServlet dispatcherServlet () {
        DispatcherServlet ds = new DispatcherServlet();
        ds.setThrowExceptionIfNoHandlerFound(true);
        ds.setDetectAllHandlerMappings(false);
        return ds;
    }

    @Bean
    public MappedInterceptor interceptor() {
        return new MappedInterceptor(null, new AppsInterceptor());
    }

}
