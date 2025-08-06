package com.neptunesoftware.venusApis.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neptunesoftware.venusApis.Beans.AppProps;
import com.neptunesoftware.venusApis.Beans.ItemCacheService;
import com.neptunesoftware.venusApis.Models.ApiResponse;
import com.neptunesoftware.venusApis.Repository.CoreDao;
import com.neptunesoftware.venusApis.Util.LicenseManager;
import com.neptunesoftware.venusApis.Util.Logging;
import com.neptunesoftware.venusApis.Util.StaticRefs;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private final AppProps appProps;
    private final LicenseManager licenseManager;
    private final ItemCacheService cacheService;
    private final Logging logger;

    public RequestInterceptor(AppProps appProps, LicenseManager licenseManager, ItemCacheService cacheService, Logging logger) {
        this.appProps = appProps;
        this.licenseManager = licenseManager;
        this.cacheService = cacheService;
        this.logger = logger;
    }

    private void interceptorResponse(HttpServletResponse response, String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .data(null)
                .response(StaticRefs.customMessage(String.valueOf(response.getStatus()), message))
                .build();

        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(apiResponse));
        writer.flush();
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        if (appProps.skipLicenseCheck.equals("Y")) {
            return true;
        } else {
            try {

                System.out.println("<< preHandle");
                String coreDt = cacheService.getCachedItem().processDt;
                String bankName = appProps.bankName;

                if (Objects.isNull(coreDt) || Objects.isNull(bankName)) {
                    logger.info("LICENSE PARAMS CHECK");
                    logger.info(coreDt);
                    logger.info(bankName);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    interceptorResponse(response, "Invalid bank parameters");
                    return false;
                }

                String license = licenseManager.decrypt(appProps.licenseToken);
                String[] validationKeys = license.split("~");
                String expiryDt = validationKeys[0];

                if (!validationKeys[1].equals(bankName) || !validationKeys[2].equals("E_COL")) {
                    logger.info("KEY PARAMS CHECK");
                    logger.info(bankName);
                    logger.info(validationKeys[2]);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    interceptorResponse(response, "Invalid Key Specified");
                    return false;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                //LocalDate expiryDate = LocalDate.parse(expiryDt, formatter);
                LocalDate coreDate = LocalDate.parse(coreDt, formatter);
                LocalDate currentDate = LocalDate.now();

                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate date = LocalDate.parse(expiryDt, inputFormatter);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedDate = date.format(outputFormatter);
                LocalDate formatedExpiryDate = LocalDate.parse(formattedDate, formatter);

                System.out.println(coreDate);
                System.out.println(formattedDate);
                System.out.println(formatedExpiryDate);

                long daysDiff = 0;
                daysDiff = 90;

                if (daysDiff <= 0) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    interceptorResponse(response, "License expired. Please contact vendor");
                    return false;
                } else {

                }

            } catch (Exception ex) {
                System.out.println(" >>> Exception in RequestInterceptor.preHandle(): " + ex.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                interceptorResponse(response, "Error occurred validating license data");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        System.out.println(" >>> Post.Processing request <<< ");

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        System.out.println(" >>> After.Finished processing request <<< ");
    }
}
