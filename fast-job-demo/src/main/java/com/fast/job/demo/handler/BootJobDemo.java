package com.fast.job.demo.handler;

import com.fast.job.core.handler.FastJobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@JobHandler("bootJobDemo")
@Component
public class BootJobDemo extends FastJobHandler {

    @Override
    protected String jobName() {
        // a name for logger pretty
        return "示例Job";
    }

    @Override
    public ReturnT<String> process(String tenantId, Object extendInfo) {
        // 此处的extendInfo从调度中心传入,调度中心不传递则为null
        try {
            doSomething(tenantId, extendInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            XxlJobLogger.log(e);
            return new ReturnT<>(ReturnT.FAIL_CODE, e.getMessage());
        }
        return ReturnT.SUCCESS;
    }

    private void doSomething(String tenantId, Object o) {
        log.info("process done.");
    }
}
