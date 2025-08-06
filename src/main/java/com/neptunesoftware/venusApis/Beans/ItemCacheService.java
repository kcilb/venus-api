package com.neptunesoftware.venusApis.Beans;


import com.neptunesoftware.venusApis.Models.CachedItems;
import com.neptunesoftware.venusApis.Repository.CoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ItemCacheService {

    private final CoreDao coreDao;
    private CachedItems cachedItems;
    private final Object lock = new Object();

    Logger logger = Logger.getLogger(ItemCacheService.class.getName());

    @Autowired
    public ItemCacheService(CoreDao coreDao) {
        this.coreDao = coreDao;
        loadCache();
    }

    private void loadCache() {
        CachedItems freshItems = coreDao.loadCacheItems();
        synchronized (lock) {
            this.cachedItems = freshItems;
        }
    }

    public CachedItems getCachedItem() {
        synchronized (lock) {
            return cachedItems;
        }
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void scheduledRefresh() {
        logger.info("Refreshing cache items");
        loadCache();
    }
}
