package com.neptunesoftware.venusApis.Repository;


import com.neptunesoftware.venusApis.Beans.AppProps;
import com.neptunesoftware.venusApis.Models.AlertCharge;
import com.neptunesoftware.venusApis.Models.CachedItems;
import com.neptunesoftware.venusApis.Models.SmsAlertCurrency;
import com.neptunesoftware.venusApis.Util.Logging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
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
                    "SELECT CRNCY_ID, CRNCY_CD, CRNCY_NM,CRNCY_CD_ISO FROM " + appProps.coreSchema +
                            ".CURRENCY WHERE REC_ST = 'A' ",
                    (rs, rowNum) -> {
                        Map<String, Object> row = new HashMap<>();
                        row.put("crncyId", rs.getInt("CRNCY_ID"));
                        row.put("crncyCd", rs.getString("CRNCY_CD"));
                        row.put("crncyIso", rs.getString("CRNCY_CD_ISO"));
                        row.put("crncyNm", rs.getString("CRNCY_NM"));
                        return row;
                    }
            );
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            throw e;
        }
    }

    public List<Map<String, Object>> findAssignableCurrencies() {
        try {
            return jdbcTemplate.query(
                    "SELECT CRNCY_ID, CRNCY_CD, CRNCY_NM,CRNCY_CD_ISO FROM " + appProps.coreSchema +
                            ".CURRENCY WHERE REC_ST = 'A' AND CRNCY_ID NOT IN " +
                            "(SELECT CRNCY_ID FROM SMS_ALERT_CURRENCY)",
                    (rs, rowNum) -> {
                        Map<String, Object> row = new HashMap<>();
                        row.put("crncyId", rs.getInt("CRNCY_ID"));
                        row.put("crncyCd", rs.getString("CRNCY_CD"));
                        row.put("crncyIso", rs.getString("CRNCY_CD_ISO"));
                        row.put("crncyNm", rs.getString("CRNCY_NM"));
                        return row;
                    }
            );
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            throw e;
        }
    }


    public List<SmsAlertCurrency> findSMSAlertCurrencies(Integer alertCrncyId) {
        try {
            return alertCrncyId == null ? jdbcTemplate.query(
                    "SELECT * FROM SMS_ALERT_CURRENCY"
                    , new BeanPropertyRowMapper(SmsAlertCurrency.class)) : jdbcTemplate.query(
                    "SELECT * FROM SMS_ALERT_CURRENCY WHERE  SMS_ALERT_CRNCY_ID = ?"
                    , new BeanPropertyRowMapper(SmsAlertCurrency.class), alertCrncyId);

        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public void createSMSAlertCurrency(SmsAlertCurrency request) {
        try {
            jdbcTemplate.update("INSERT INTO SMS_ALERT_CURRENCY(CRNCY_ISO,CRNCY_NM, CREATED_BY,CRNCY_ID,STATUS) VALUES(?,?,?,?,?)",
                    request.getCrncyIso(),request.getCrncyNm(),request.getCreatedBy(), request.getCrncyId(), request.getStatus());
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            throw e;
        }
    }

    public void updateSMSAlertCurrency(SmsAlertCurrency request) {
        try {
            jdbcTemplate.update("UPDATE SMS_ALERT_CURRENCY SET STATUS = ? WHERE SMS_ALERT_CRNCY_ID = ?",
                    request.getStatus(), request.getSmsAlertCrncyId());
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
