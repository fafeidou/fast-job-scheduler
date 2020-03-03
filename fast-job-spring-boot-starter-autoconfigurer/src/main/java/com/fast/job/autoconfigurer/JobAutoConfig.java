package com.fast.job.autoconfigurer;

import com.fast.job.autoconfigurer.props.HealthCheckProperties;
import com.fast.job.autoconfigurer.props.JobExecutorProperties;
import com.fast.job.autoconfigurer.props.JobSchedulerProperties;
import com.fast.job.core.filter.HealthCheckFilter;
import com.google.common.collect.Lists;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        JobSchedulerProperties.class,
        JobExecutorProperties.class,
        HealthCheckProperties.class
})
@Slf4j
public class JobAutoConfig {

    @Autowired
    private JobSchedulerProperties schedulerProperties;

    @Autowired
    private JobExecutorProperties executorProperties;

    @Autowired
    private HealthCheckProperties healthCheckProperties;

    @Bean(initMethod = "start", destroyMethod = "destroy")
    @ConditionalOnMissingBean(XxlJobSpringExecutor.class)
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job auto config init start .");

        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(schedulerProperties.getAddress());
        xxlJobSpringExecutor.setAppName(executorProperties.getAppName());
        xxlJobSpringExecutor.setIp(executorProperties.getIp());
        xxlJobSpringExecutor.setPort(executorProperties.getPort());
        xxlJobSpringExecutor.setAccessToken(executorProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(executorProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(executorProperties.getLogRetentionDays());

        log.info(">>>>>>>>>>> xxl-job auto config init done .");
        return xxlJobSpringExecutor;
    }


    @Bean
    @ConditionalOnProperty(name = "fast.job.health-check.enabled", havingValue = "true")
    public FilterRegistrationBean healthCheckFilter() {
        log.info("<<<<<< xxl-job auto config health-check filter registered");
        HealthCheckFilter filter = new HealthCheckFilter(healthCheckProperties.getUri(), healthCheckProperties.getResponseText());
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setUrlPatterns(Lists.newArrayList(healthCheckProperties.getUri()));
        registrationBean.setOrder(healthCheckProperties.getFilterOrder());
        return registrationBean;
    }
}
