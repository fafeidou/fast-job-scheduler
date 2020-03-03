package com.xxl.job.admin.core.util;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;
import lombok.extern.slf4j.Slf4j;

/**
 * ftl util
 *
 * @author xuxueli 2018-01-17 20:37:48
 */
@Slf4j
public class FtlUtil {

    private static BeansWrapper wrapper = new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();     //BeansWrapper.getDefaultInstance();

    public static TemplateHashModel generateStaticModel(String packageName) {
        try {
            TemplateHashModel staticModels = wrapper.getStaticModels();
            TemplateHashModel fileStatics = (TemplateHashModel) staticModels.get(packageName);
            return fileStatics;
        } catch (Exception e) {
            log.warn("FtlUtil generateStaticModel error", e);
        }
        return null;
    }

}
