
package com.benjaminbatte.platform.common.web;

import com.benjaminbatte.platform.common.web.versioning.ApiV1;
import com.benjaminbatte.platform.common.web.versioning.ApiV2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/v1", c -> c.isAnnotationPresent(ApiV1.class));
        configurer.addPathPrefix("/v2", c -> c.isAnnotationPresent(ApiV2.class));
    }
}
