package com.neptunesoftware.venusApis.Services;

import com.neptunesoftware.venusApis.Models.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AlertChannelService {

    private Logger logger = Logger.getLogger(AlertChannelService.class.getName());

    public ApiResponse<?> findTransactionAlerts(String lastMsgId) {

        try {

        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return ApiResponse.builder().build();
    }

    public ApiResponse<?> updateAccountStats(String acctNo, String msgId) {
        try {

        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return ApiResponse.builder().build();
    }
}
