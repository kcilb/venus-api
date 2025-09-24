package com.neptunesoftware.venusApis.Beans;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@Data
public class AppProps {

    @Value("${app.venus.coreSchema}")
    public String coreSchema;

    @Value("${app.venus.auth.username}")
    public String username;

    @Value("${app.venus.auth.password}")
    public String password;

    @Value("${app.venus.bankName}")
    public String bankName;

    @Value("${app.venus.licenseToken}")
    public String licenseToken;

    @Value("${app.venus.fetchLimit}")
    public int fetchLimit;

    @Value("${app.venus.callableTasks}")
    public String callableTasks;

    @Value("${app.venus.pendingCharge}")
    public String pendingCharge;

    @Value("${app.venus.failedCharge}")
    public String failedCharge;

    @Value("${app.venus.taxChargeGl}")
    public String taxChargeGl;

    @Value("${app.venus.bankChargeGl}")
    public String bankChargeGl;

    @Value("${app.venus.vendorChargeGl}")
    public String vendorChargeGl;

    @Value("${app.venus.rubiApi}")
    public String rubiApi;

    @Value("${app.venus.skipLicenseCheck}")
    public String skipLicenseCheck;

    @Value("${io.config.xapiFilePath}")
    public String xapiFilePath;



}
