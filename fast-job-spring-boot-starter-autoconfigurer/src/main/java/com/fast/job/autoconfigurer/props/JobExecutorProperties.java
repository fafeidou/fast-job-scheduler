package com.fast.job.autoconfigurer.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fast.job.executor")
public class JobExecutorProperties {
    /**
     * 执行器名
     */
    private String appName;
    private String ip;
    /**
     * 调度中心和执行器的通信端口
     */
    private Integer port = 9999;
    private String accessToken;
    private String logPath = "/data/applogs/xxl-job/jobhandler/" + appName;
    private Integer logRetentionDays = -1;
}
