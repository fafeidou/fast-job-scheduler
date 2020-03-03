package com.fast.job.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fast.job.core.domain.JobParam;
import com.google.common.collect.Lists;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 泛型参数T接受以下格式的自定义参数:
 * <pre>
 * {
 * 	"tenantIdList" : ["db01","db02", ...],
 * 	"extendInfo" : [{}, {}, ...]
 * }
 * 或
 * {
 * 	"tenantIdList" : ["db01","db02", ...],
 * 	"extendInfo" : {}
 * }
 * </pre>
 *
 * @param <T> 客户自定义参数，可通过XXL-JOB调度中心传递
 */
@Slf4j
public abstract class FastJobHandler<T> extends IJobHandler {

    @Value("${vms_all_tenant_list:}")
    private String tenantIdList;

    /**
     * 为了方便从日志中区分job
     *
     * @return job业务名
     */
    protected String jobName() {
        return "";
    }

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        JobParam<T> jobParam = this.parseParam(param);

        log.info("original params:{}", param);
        List<String> tenantIdList = this.parseTenantIdList(jobParam);
        log.info("parsed params:{}", jobParam);
        log.info("parsed tenantIdList:{}", tenantIdList);

        if (CollectionUtils.isEmpty(tenantIdList)) {
            XxlJobLogger.log("ERROR: tenantIdList is empty !!!!");
            return new ReturnT<>(ReturnT.FAIL_CODE, "tenantIdList is empty");
        }

        ReturnT<String> returnT = ReturnT.FAIL;
        for (String id : tenantIdList) {
            if (StringUtils.isEmpty(id)) {
                continue;
            }
            try {
                log.info(">>>>>>[{}] -- 「{}」 start", jobName(), id);
                XxlJobLogger.log(">>>>>>[{}] -- 「{}」 start", jobName(), id);
                returnT = process(id, jobParam.getExtendInfo());
            } finally {
                log.info("<<<<<<[{}] -- 「{}」 end\n", jobName(), id);
                XxlJobLogger.log("<<<<<<[{}] -- 「{}」 end\n", jobName(), id);
            }
        }

        return returnT;
    }

    protected List<String> parseTenantIdList() {
        if (StringUtils.isEmpty(tenantIdList)) {
            return Lists.newArrayList();
        }
        final List<String> idList;

        try {
            // [{u'admin_domain': u'unilever-top.tezign.com', u'index': 0, u'web_domain': u'unilever.tezign.com', u'tenant_id': u't1'}, {u'admin_domain': u'vms-t2-top.tezign.com', u'index': 1, u'web_domain': u'vms-t2.tezign.com', u'tenant_id': u't2'}]
            String str = tenantIdList.replaceAll("u'", "'");
            JSONArray array = JSON.parseArray(str);
            idList = array.stream()
                    .map(o -> ((JSONObject) o).getString("tenant_id"))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            XxlJobLogger.log(e);
            log.info("解析租户列表出错", e);
            return Lists.newArrayList();
        }
        log.info("load all tenant id list : {}", idList);
        return idList;
    }

    private List<String> parseTenantIdList(JobParam<T> jobParam) {
        if (jobParam == null) {
            jobParam = new JobParam<>();
        }
        if (CollectionUtils.isEmpty(jobParam.getTenantIdList())) {
            log.info("tenantIdList is empty, load all ...");
            jobParam.setTenantIdList(this.loadAllTenantList());
            return jobParam.getTenantIdList();
        }
        return jobParam.getTenantIdList();
    }

    private List<String> loadAllTenantList() {
        return this.parseTenantIdList();
    }

    public abstract ReturnT<String> process(String tenantId, T t);

    private JobParam<T> parseParam(String param) {
        if (StringUtils.isEmpty(param)) {
            log.debug("param is empty");
            return new JobParam<>();
        }
        return JSON.parseObject(param, new TypeReference<JobParam<T>>() {
        });
    }

}
