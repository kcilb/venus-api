package com.neptunesoftware.venusApis.Repository;


import com.neptunesoftware.venusApis.Beans.AppProps;
import com.neptunesoftware.venusApis.Models.AlertCharge;
import com.neptunesoftware.venusApis.Models.CachedItems;
import com.neptunesoftware.venusApis.Models.ChargeTiers;
import com.neptunesoftware.venusApis.Models.SmsAlertCurrency;
import com.neptunesoftware.venusApis.Util.Logging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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
            item.smsCurrencyList = findSMSAlertCurrencies(null)
                    .stream().filter(f -> Objects.equals(f.getStatus(), "A"))
                    .collect(Collectors.toList());
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
                    request.getCrncyIso(), request.getCrncyNm(), request.getCreatedBy(), request.getCrncyId(), request.getStatus());
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
                return jdbcTemplate.query("SELECT * FROM SMS_CHARGE_TIERS WHERE STATUS = 'A'", new BeanPropertyRowMapper(AlertCharge.class));
            } else {
                return jdbcTemplate.query("SELECT * FROM SMS_CHARGE_TIERS WHERE SMS_ALERT_CRNCY_ID = ?", new BeanPropertyRowMapper(AlertCharge.class), smsAlertCrncyId);
            }
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public void createCharge(List<ChargeTiers> requestList) {
        try {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO SMS_CHARGE_TIERS (CHARGE_DESC, TXN_TYPE, MIN_VALUE, MAX_VALUE, " +
                            "VENDOR_CHARGE, BANK_CHARGE, EXCISE_CHARGE, STATUS, MODIFIED_BY, " +
                            "MODIFIED_DATE, SMS_ALERT_CRNCY_ID) " +
                            "VALUES('SMS Charge Tier', 'SMS', ?, ?, ?, ?, ?, 'A', 'SYSTEM', SYSDATE, ?)",
                    requestList,
                    requestList.size(),
                    (ps, request) -> {
                        ps.setBigDecimal(1, request.getMinValue());
                        ps.setBigDecimal(2, request.getMaxValue());
                        ps.setBigDecimal(3, request.getVendorCharge());
                        ps.setBigDecimal(4, request.getBankCharge());
                        ps.setBigDecimal(5, request.getExciseCharge());
                        ps.setLong(6, request.getSmsAlertCrncyId());
                    }
            );
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            throw e;
        }
    }

    public void updateCharge(ChargeTiers request) {
        try {
            jdbcTemplate
                    .update("UPDATE SMS_CHARGES SET VENDOR_CHARGE = ?, BANK_CHARGE = ?," +
                                    " EXCISE_CHARGE = ? WHERE SMS_ALERT_CRNCY_ID = ? AND ID = ?)"
                            , request.getVendorCharge(), request.getExciseCharge(), request.getBankCharge(),
                            request.getSmsAlertCrncyId(), request.getId());
        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            throw e;
        }
    }

    public void removeCharge(List<ChargeTiers> requests) {
        try {
            jdbcTemplate.batchUpdate(
                    "DELETE FROM SMS_CHARGE_TIERS WHERE SMS_ALERT_CRNCY_ID = ?",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ChargeTiers request = requests.get(i);
                            ps.setLong(1, request.getSmsAlertCrncyId());
                        }

                        @Override
                        public int getBatchSize() {
                            return requests.size();
                        }
                    }
            );
        } catch (Exception e) {
            Logging.error("Failed to batch delete charge tiers: " + e.getMessage(), e);
            throw e;
        }
    }

}
