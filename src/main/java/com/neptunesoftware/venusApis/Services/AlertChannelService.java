package com.neptunesoftware.venusApis.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neptunesoftware.venusApis.Beans.ItemCacheService;
import com.neptunesoftware.venusApis.Models.TrxnSmsList;
import com.neptunesoftware.venusApis.Models.Update;
import com.neptunesoftware.venusApis.Repository.AlertsDao;
import com.neptunesoftware.venusApis.Util.Logging;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AlertChannelService {

    private final ItemCacheService itemCacheService;
    private final AlertsDao alertsDao;

    public AlertChannelService(ItemCacheService itemCacheService, AlertsDao alertsDao) {
        this.itemCacheService = itemCacheService;
        this.alertsDao = alertsDao;
    }

    public TrxnSmsList findTransactionAlerts(String body) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String lastMsgId = mapper.readValue(body, String.class);
            return alertsDao.findTransactionAlerts(lastMsgId);
        } catch (Exception e) {
            Logging.error(e.getMessage(),e);
            return new TrxnSmsList("96",
                    "An error occurred while processing your request.", null);
        }

    }

    public Update updateAccountStats(String body) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> map = mapper.readValue(body, Map.class);
            String acctNo = (String) map.get("acctNo");
            Integer msgCount = (Integer)map.get("msgCount");

            return alertsDao.updateAccountStats(acctNo, msgCount, itemCacheService.getCachedItem().processDt);
        } catch (Exception e) {
            Logging.error(e.getMessage(),e);
            return new Update("96",
                    "An error occurred while processing your request ", null);
        }
    }
}
