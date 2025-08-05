package com.neptunesoftware.venusApis.Beans;

import com.neptunesoftware.venusApis.Repository.CoreDao;
import com.neptunesoftware.venusApis.Security.RequestInterceptor;
import com.neptunesoftware.venusApis.Util.LicenseManager;
import com.neptunesoftware.venusApis.Util.Logging;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {

    private final AppProps appProps;
    private final LicenseManager licenseManager;
    private final CoreDao coreDao;
    private final Logging logger;

    public InterceptorConfig(AppProps appProps, LicenseManager licenseManager, CoreDao coreDao, Logging logger) {
        this.appProps = appProps;
        this.licenseManager = licenseManager;
        this.coreDao = coreDao;
        this.logger = logger;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor(appProps,licenseManager,coreDao,logger));
    }
}
