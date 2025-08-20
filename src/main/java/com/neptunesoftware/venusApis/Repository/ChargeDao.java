package com.neptunesoftware.venusApis.Repository;

import com.neptunesoftware.venusApis.Models.SMSChargeLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class ChargeDao {

    private final JdbcTemplate jdbcTemplate;

    public ChargeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SMSChargeLog> findChargeHistory(Date startDate, Date endDate) {
        try {
            String sql = "SELECT TOTAL_ACCOUNTS, LOW_FUNDS_COUNT, PROCESSED_COUNT, " +
                    "FAILED_COUNT, RECOVERED_AMT, CHARGE_DESC, CREATE_DT, STATUS " +
                    "FROM SMSBANK.SMS_CHARGE_LOG " +
                    "WHERE CREATE_DT BETWEEN ? AND ?";

            List<SMSChargeLog> results = jdbcTemplate.query(
                    sql,
                    new Object[]{
                            new java.sql.Timestamp(startDate.getTime()),
                            new java.sql.Timestamp(endDate.getTime())
                    },
                    new RowMapper<SMSChargeLog>() {
                        @Override
                        public SMSChargeLog mapRow(ResultSet rs, int rowNum) throws SQLException {
                            SMSChargeLog log = new SMSChargeLog();
                            log.setTotalAccounts(rs.getInt("TOTAL_ACCOUNTS"));
                            log.setLowFundsCount(rs.getInt("LOW_FUNDS_COUNT"));
                            log.setProcessedCount(rs.getInt("PROCESSED_COUNT"));
                            log.setFailedCount(rs.getInt("FAILED_COUNT"));
                            log.setRecoveredAmt(rs.getBigDecimal("RECOVERED_AMT"));
                            log.setChargeDesc(rs.getString("CHARGE_DESC"));
                            log.setCreateDt(rs.getDate("CREATE_DT"));
                            log.setStatus(rs.getString("STATUS"));
                            return log;
                        }
                    }
            );
            return !results.isEmpty() ? results : Collections.emptyList();
        } catch (Exception e) {
            throw e;
        }
    }
}
