package com.neptunesoftware.venusApis.Repository;

import com.neptunesoftware.venusApis.Beans.AppProps;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CoreDao {

    private final JdbcTemplate jdbcTemplate;
    private final AppProps appProps;

    public CoreDao(JdbcTemplate jdbcTemplate, AppProps appProps) {
        this.jdbcTemplate = jdbcTemplate;
        this.appProps = appProps;
    }

    public String findProcessingDt() {
        return jdbcTemplate.queryForObject("SELECT DISPLAY_VALUE FROM " + appProps.coreSchema + " PARAM_CD = 'S02'", String.class);
    }
}
