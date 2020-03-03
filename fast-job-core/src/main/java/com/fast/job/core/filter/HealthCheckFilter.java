package com.fast.job.core.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class HealthCheckFilter implements Filter {
    private String uri;
    private String respText;

    public HealthCheckFilter(String uri, String respText) {
        this.uri = uri;
        this.respText = respText;
    }


    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (isHealthCheckUri(request)) {
            servletResponse.getWriter().print(respText);
            servletResponse.getWriter().flush();
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {

    }

    private boolean isHealthCheckUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return StringUtils.isNotBlank(uri)
                && this.uri.equalsIgnoreCase(uri);
    }


}
