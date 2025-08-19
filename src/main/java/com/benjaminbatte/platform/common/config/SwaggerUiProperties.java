package com.benjaminbatte.platform.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "springdoc.swagger-ui")
public class SwaggerUiProperties {
    private Boolean enabled;
    private String tagsSorter;
    private String operationsSorter;
    private String path;

}