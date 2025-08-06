package com.neptunesoftware.venusApis.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neptunesoftware.venusApis.Beans.ItemCacheService;
import com.neptunesoftware.venusApis.Models.ApiResponse;
import com.neptunesoftware.venusApis.Models.TrxnSmsList;
import com.neptunesoftware.venusApis.Models.Update;
import com.neptunesoftware.venusApis.Repository.CoreDao;
import com.neptunesoftware.venusApis.Util.StaticRefs;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.logging.Logger;

@Service
public class AlertChannelService {

    private Logger logger = Logger.getLogger(AlertChannelService.class.getName());

    private final ItemCacheService itemCacheService;
    private final CoreDao coreDao;

    public AlertChannelService(ItemCacheService itemCacheService, CoreDao coreDao) {
        this.itemCacheService = itemCacheService;
        this.coreDao = coreDao;
    }

    public TrxnSmsList findTransactionAlerts(String body) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String lastMsgId = mapper.readValue(body, String.class);
            return coreDao.findTransactionAlerts(lastMsgId);
        } catch (Exception e) {
            logger.info(e.getMessage());
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

            return coreDao.updateAccountStats(acctNo, msgCount, itemCacheService.getCachedItem().processDt);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new Update("96",
                    "An error occurred while processing your request ", null);
        }
    }
}
