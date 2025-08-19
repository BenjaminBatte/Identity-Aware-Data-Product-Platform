package com.benjaminbatte.platform.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.features")
public class FeaturesProperties {
    // Getters and setters
    private boolean enableCaching = true;
    private boolean enableAsyncProcessing = true;

}