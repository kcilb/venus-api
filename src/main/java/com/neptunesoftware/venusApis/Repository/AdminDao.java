package com.neptunesoftware.venusApis.Repository;


import com.neptunesoftware.venusApis.Beans.AppProps;
import com.neptunesoftware.venusApis.Models.AlertCharge;
import com.neptunesoftware.venusApis.Models.CachedItems;
import com.neptunesoftware.venusApis.Util.Logging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
            item.chargesList = findCharges(null);
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

    public List<Map<String, Object>> findInstitutionCurrencies() {
        try {
            return jdbcTemplate.query(
                    "SELECT CRNCY_ID,CRNCY_CD, CRNCY_NM FROM " + appProps.coreSchema + ".CURRENCY WHERE REC_ST = 'A'" +
                            "AND CRNCY_ID NOT IN (SELECT CRNCY_ID FROM SMS_ALERT_CRNCY)"
                    , new BeanPropertyRowMapper(Map.class));
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            throw e;
        }
    }


    public List<Map<String, Object>> findSMSAlertCurrencies(Integer alertCrncyId) {
        try {
            return alertCrncyId == null ? jdbcTemplate.query(
                    "SELECT * FROM SMS_ALERT_CRNCY WHERE STATUS = 'A'"
                    , new BeanPropertyRowMapper(Map.class)) : jdbcTemplate.query(
                    "SELECT * FROM SMS_ALERT_CRNCY WHERE  SMS_ALERT_CRNCY_ID = ?"
                    , new BeanPropertyRowMapper(Map.class), alertCrncyId);

        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public void createSMSAlertCurrency(Map<String, Object> map) {
        try {
            jdbcTemplate.update("INSERT INTO SMS_ALERT_CRNCY(CRNCY_ISO,CRNCY_ID,STATUS) VALUE(?,?,?)",
                    map.get("crncy_iso"), map.get("crncy_id"), map.get("status"));
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            throw e;
        }
    }

    public void updateSMSAlertCurrency(Map<String, Object> map) {
        try {
            jdbcTemplate.update("UPDATE SMS_ALERT_CRNCY SET STATUS = ? WHERE SMS_ALERT_CRNCY_ID = ?",
                    map.get("status"), map.get("sms_alert_crncy_id"));
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            throw e;
        }
    }

    public List<AlertCharge> findCharges(Integer smsAlertCrncyId) {
        try {
            if (smsAlertCrncyId == null) {
                return jdbcTemplate.query("SELECT * FROM SMS_CHARGES", new BeanPropertyRowMapper(AlertCharge.class));
            } else {
                return jdbcTemplate.query("SELECT * FROM SMS_CHARGES WHERE SMS_ALERT_CRNCY_ID = ?", new BeanPropertyRowMapper(AlertCharge.class), smsAlertCrncyId);
            }
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public void createCharge(Map<String, Object> map) {
        try {
            jdbcTemplate
                    .update("INSERT INTO SMS_CHARGES (CHARGEDESC, TXNTYPE, MIN_VALUE, MAX_VALUE, VENDOR_CHARGE, BANK_CHARGE," +
                                    " EXCISE_CHARGE, STATUS, MODIFIEDBY, MODIFIEDDATE, SMS_ALERT_CRNCY_ID)" +
                                    " VALUES('SMS Charge Tier', 'S', ?,?,?,?,?,'A', 'SYSTEM', SYSDATE, ?)"
                            , map.get("tierMin"), map.get("tierMax"), map.get("vendorCharge"), map.get("bankCharge"), map.get("taxCharge")
                            , map.get("sms_alert_crncy_id"));
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            throw e;
        }
    }

    public void removeCharge(Map<String, Object> map) {
        jdbcTemplate
                .update("DELETE FROM SMS_CHARGES WHERE PTID = ?", map.get("ptid"));
    }

}
