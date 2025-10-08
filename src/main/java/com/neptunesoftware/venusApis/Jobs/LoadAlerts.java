package com.neptunesoftware.venusApis.Jobs;

import com.neptunesoftware.venusApis.Beans.ItemCacheService;
import com.neptunesoftware.venusApis.Repository.AdminDao;
import com.neptunesoftware.venusApis.Repository.AlertsDao;
import com.neptunesoftware.venusApis.Util.Logging;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Async
@Service
public class LoadAlerts {

    private final ItemCacheService itemCacheService;
    private final AdminDao adminDao;

    public LoadAlerts(ItemCacheService itemCacheService, AdminDao adminDao) {
        this.itemCacheService = itemCacheService;
        this.adminDao = adminDao;
    }

    @Scheduled(cron = "${app.venus.callableSvc}")
    protected void loadCallableAlerts() {
        Logging.info("================RUNNING_CALLABLE_SERVICES===============");
        Logging.info("Running scheduled callable services");
        for (String task : itemCacheService.getCachedItem().callableTasks) {
            try {
                Logging.info("RUNNING TASK: " + task);
                adminDao.executeCallableService(task);
            } catch (Exception e) {
                Logging.info(e.getMessage());
            }
        }
        Logging.info("Completed scheduled callable services");
    }
}
