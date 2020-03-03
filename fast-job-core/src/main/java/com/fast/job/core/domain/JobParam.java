package com.fast.job.core.domain;

import lombok.Data;

import java.util.List;

@Data
public class JobParam<T> {
    private List<String> tenantIdList;
    private T extendInfo;
}
