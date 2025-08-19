package com.benjaminbatte.platform.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.rate-limiting")
public class RateLimitingProperties {
    private boolean enabled = true;
    private int requestsPerSecond = 100;

}