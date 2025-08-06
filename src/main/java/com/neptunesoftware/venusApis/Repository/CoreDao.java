package com.neptunesoftware.venusApis.Repository;

import com.neptunesoftware.venusApis.Beans.AppProps;
import com.neptunesoftware.venusApis.Models.CachedItems;
import com.neptunesoftware.venusApis.Models.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.logging.Logger;

@Repository
public class CoreDao {

    private final JdbcTemplate jdbcTemplate;
    private final AppProps appProps;
    Logger logger = Logger.getLogger(CoreDao.class.getName());

    public CoreDao(JdbcTemplate jdbcTemplate, AppProps appProps) {
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
            return item;
        } catch (Exception e) {
            logger.info("Failed to load cache items" + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }


    public Update updateAccountStats(String acctNo, int msgCount) {
        return new Update("92", "No updates were performed", null);
//        try (Connection conn = XapiServices.getConnection();
//             PreparedStatement updateStmt = conn
//                     .prepareStatement("update sms_count set sms_count = ?, total_count = ?, chrge_status =? where acct_no = ? and log_date >= trunc((select to_date(display_value,'dd/mm/yyyy') from "
//                             + XapiSettings.coreschema
//                             + ".ctrl_parameter where param_cd = 'S02'),'mm')");
//             PreparedStatement insertStmt = conn
//                     .prepareStatement("insert into sms_count(sms_count,total_count,chrge_status,acct_no, log_date) values(?,?,?,?,(select to_date(display_value,'dd/mm/yyyy') from "
//                             + XapiSettings.coreschema
//                             + ".ctrl_parameter where param_cd = 'S02'))")) {
//
//            QueryRunner queryRunner = new QueryRunner();
//            Map<String, Object> map = queryRunner.query(conn,
//                    "select * from v_alert_count where acct_no = ?",
//                    new MapHandler(), acctNo);
//
//            int updateCount = 0;
//            if (map != null && !map.isEmpty()) {
//                int totalSMS = Integer.parseInt(String.valueOf(map
//                        .get("sms_count"))) + msgCount;
//                updateStmt.setInt(1, totalSMS);
//                updateStmt.setInt(2, totalSMS);
//                updateStmt.setString(3, "N");
//                updateStmt.setString(4, acctNo);
//                updateCount = updateStmt.executeUpdate();
//            } else {
//                insertStmt.setInt(1, msgCount);
//                insertStmt.setInt(2, msgCount);
//                insertStmt.setString(3, "N");
//                insertStmt.setString(4, acctNo);
//                updateCount = insertStmt.executeUpdate();
//            }
//            if (updateCount >= 0) {
//                return new Update("0", "Success", null);
//            }
//            return new Update("92", "No updates were performed", null);
//        } catch (Exception e) {
//            return new Update("96",
//                    "An error occurred while processing your request "
//                            + e.getLocalizedMessage(), null);
//        } finally {
//            logger.info(
//                    "updateAccountStats account number: " + acctNo
//                            + " last sent out alerts count " + msgCount);
//        }
    }


}
