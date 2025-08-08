package com.neptunesoftware.venusApis.Beans;

import com.neptunesoftware.venusApis.Security.RequestInterceptor;
import com.neptunesoftware.venusApis.Util.LicenseManager;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {

    private final AppProps appProps;
    private final LicenseManager licenseManager;
    private final ItemCacheService cacheService;

    public InterceptorConfig(AppProps appProps, LicenseManager licenseManager,
                             ItemCacheService cacheService) {
        this.appProps = appProps;
        this.licenseManager = licenseManager;
        this.cacheService = cacheService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor(appProps,licenseManager,cacheService));
    }
}
