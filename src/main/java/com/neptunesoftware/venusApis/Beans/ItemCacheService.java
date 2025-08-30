package com.neptunesoftware.venusApis.Beans;


import com.neptunesoftware.venusApis.Models.CachedItems;
import com.neptunesoftware.venusApis.Models.SmsAlertCurrency;
import com.neptunesoftware.venusApis.Repository.AdminDao;
import com.neptunesoftware.venusApis.Repository.AlertsDao;
import com.neptunesoftware.venusApis.Util.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ItemCacheService {

    private final AdminDao adminDao;
    private CachedItems cachedItems;
    private final Object lock = new Object();

    @Autowired
    public ItemCacheService(AdminDao adminDao) {
        this.adminDao = adminDao;
        loadCache();
    }

    private void loadCache() {
        CachedItems freshItems = adminDao.loadCacheItems();
        synchronized (lock) {
            this.cachedItems = freshItems;
        }
    }

    public CachedItems getCachedItem() {
        synchronized (lock) {
            return cachedItems;
        }
    }

    public String getCurrencyNameById(Integer currencyId) {
        return getCachedItem().smsCurrencyList.stream()
                .filter(currency -> currencyId.equals(currency.getSmsAlertCrncyId())
                )
                .findFirst()
                .map(SmsAlertCurrency::getCrncyNm)
                .orElse("Currency not found");
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void scheduledRefresh() {
        Logging.info("Refreshing cache items");
        loadCache();
    }
}
