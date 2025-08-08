package com.neptunesoftware.venusApis.Repository;


import com.neptunesoftware.venusApis.Beans.AppProps;
import com.neptunesoftware.venusApis.Models.CachedItems;
import com.neptunesoftware.venusApis.Util.Logging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class AdminDao {

    private final JdbcTemplate jdbcTemplate;
    private final AppProps appProps;

    public AdminDao(JdbcTemplate jdbcTemplate, AppProps appProps) {
        this.jdbcTemplate = jdbcTemplate;
        this.appProps = appProps;
    }

    public String findProcessingDt() {
        return jdbcTemplate.queryForObject("SELECT DISPLAY_VALUE FROM " + appProps.coreSchema + ".ctrl_parameter WHERE PARAM_CD = 'S02'", String.class);
    }

    public CachedItems loadCacheItems() {
        try {
            CachedItems item = new CachedItems();
            item.processDt = findProcessingDt();
            item.callableTasks = appProps.callableTasks.split(",");
            return item;
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    public void executeCallableService(String task) {
        try {
            jdbcTemplate.execute("{call " + task + "}");
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
        }
    }

}
