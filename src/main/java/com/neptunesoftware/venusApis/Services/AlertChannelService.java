package com.neptunesoftware.venusApis.Services;

import com.neptunesoftware.venusApis.Models.ApiResponse;
import com.neptunesoftware.venusApis.Models.TrxnSmsList;
import com.neptunesoftware.venusApis.Repository.CoreDao;
import com.neptunesoftware.venusApis.Util.StaticRefs;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AlertChannelService {

    private Logger logger = Logger.getLogger(AlertChannelService.class.getName());

    private final CoreDao coreDao;

    public AlertChannelService(CoreDao coreDao) {
        this.coreDao = coreDao;
    }

    public TrxnSmsList findTransactionAlerts(String lastMsgId) {
        try {
            return new TrxnSmsList().createDummyResponse();
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new TrxnSmsList("96",
                    "An error occurred while processing your request.", null);
        }

    }

    public ApiResponse<?> updateAccountStats(String acctNo, String msgId) {
        try {

        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return ApiResponse.builder().build();
    }
}
