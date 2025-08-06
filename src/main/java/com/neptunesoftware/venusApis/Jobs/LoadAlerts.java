package com.neptunesoftware.venusApis.Jobs;

import com.neptunesoftware.venusApis.Beans.ItemCacheService;
import com.neptunesoftware.venusApis.Repository.CoreDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Async
@Service
public class LoadAlerts {

    private final ItemCacheService itemCacheService;
    private final CoreDao coreDao;
    Logger logger = Logger.getLogger(LoadAlerts.class.getName());

    public LoadAlerts(ItemCacheService itemCacheService, CoreDao coreDao) {
        this.itemCacheService = itemCacheService;
        this.coreDao = coreDao;
    }

    @Scheduled(cron = "${app.venus.callableSvc}")
    protected void loadCallableAlerts() {
        logger.info("Running scheduled callable services");
        for (String task : itemCacheService.getCachedItem().callableTasks) {
            try {
                coreDao.executeCallableService(task);
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        logger.info("Completed scheduled callable services");
    }
}
