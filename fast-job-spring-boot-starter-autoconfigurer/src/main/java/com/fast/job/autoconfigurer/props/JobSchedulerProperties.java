package com.fast.job.autoconfigurer.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fast.job.scheduler")
@Setter
@Getter
public class JobSchedulerProperties {
    /**
     * 调度中心地址
     */
    private String address = "http://localhost:8080/xxl-job-admin";
}
