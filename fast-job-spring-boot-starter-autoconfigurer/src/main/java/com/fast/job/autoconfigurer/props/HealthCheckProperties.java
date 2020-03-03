package com.fast.job.autoconfigurer.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fast.job.health-check")
public class HealthCheckProperties {
    /**
     * 是否启用健康检查
     */
    private Boolean enabled = true;
    /**
     * 健康检查暴露的URI
     */
    private String uri = "/healthcheck";
    /**
     * 返回给client的内容
     */
    private String responseText = "true";
    /**
     * filter顺序
     */
    private Integer filterOrder = 1;
}
