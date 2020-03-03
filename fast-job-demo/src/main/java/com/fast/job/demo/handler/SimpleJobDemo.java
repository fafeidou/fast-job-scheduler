package com.fast.job.demo.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@JobHandler("simpleJobDemo")
public class SimpleJobDemo extends IJobHandler {
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        // 此处的extendInfo从调度中心传入,调度中心不传递则为null
        log.info("job start ...");
        try {
            log.info("processing...");
        } catch (Exception e) {
            log.error("", e);
            XxlJobLogger.log(e);
            return new ReturnT<>(ReturnT.FAIL_CODE, e.getMessage());
        }
        log.info("job end ...");
        return ReturnT.SUCCESS;
    }
}
