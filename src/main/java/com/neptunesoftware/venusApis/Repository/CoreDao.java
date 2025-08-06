package com.neptunesoftware.venusApis.Repository;

import com.neptunesoftware.venusApis.Beans.AppProps;
import com.neptunesoftware.venusApis.Models.CachedItems;
import com.neptunesoftware.venusApis.Models.SMS;
import com.neptunesoftware.venusApis.Models.TrxnSmsList;
import com.neptunesoftware.venusApis.Models.Update;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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


//    public TrxnSmsList loadTrxnAlerts(String lastMsgId) {
//        TrxnSmsList tranList;
//        try (Connection conn = XapiServices.getConnection()) {
//            QueryRunner queryRunner = new QueryRunner();
//            List<SMS> mapList = queryRunner
//                    .query(conn,
//                            "select * from v_outward_messages_dep where recordID > ? and rownum <= ?",
//                            new BeanListHandler<SMS>(
//                                    SMS.class),
//                            ((lastMsgId != null)
//                                    && (lastMsgId.trim().length() > 0) ? lastMsgId
//                                    : "0"), XapiSettings.fetchlimit);
//            if (mapList != null && !mapList.isEmpty())
//                tranList = new TrxnSmsList("0", "Success", mapList);
//            else
//                tranList = new TrxnSmsList("21", "No records available", null);
//        } catch (Exception e) {
//            XapiLogger.getLogger().error(e);
//            tranList = new TrxnSmsList("96",
//                    "An error occcured while processing your request.", null);
//        } finally {
//            XapiLogger.getLogger().info(
//                    "Last retrieved transaction alert id: " + lastMsgId);
//        }
//        return tranList;
//    }

    public Optional<Map<String, Object>> getAlertCount(String acctNo) {
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(
                    "SELECT * FROM v_alert_count WHERE acct_no = ?",
                    acctNo
            );
            return Optional.of(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Transactional
    public Update updateAccountStats(String acctNo, int msgCount, String processDt) {
        try {
            String updateSql = "update sms_count set sms_count = ?, total_count = ?, chrge_status =? where acct_no = ? ";

            String insertSql = "insert into sms_count(sms_count,total_count,chrge_status,acct_no,log_date) " +
                    "values(?,?,?,?,?)";


            Optional<Map<String, Object>> map = getAlertCount(acctNo);
            int updateCount = 0;
            if (map.isPresent()) {
                int totalSMS = Integer.parseInt(String.valueOf(map
                        .get().get("sms_count"))) + msgCount;
                updateCount = jdbcTemplate.update(updateSql, totalSMS, totalSMS, "N", acctNo);
            } else {
                updateCount = jdbcTemplate.update(insertSql, msgCount, msgCount, "N",acctNo, processDt);
            }

            if (updateCount >= 0) {
                return new Update("00", "Success", null);
            }
            return new Update("92", "No updates were performed", null);
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return new Update("96",
                    "An error occurred while processing your request "
                            + e.getLocalizedMessage(), null);
        } finally {
            logger.info(
                    "updateAccountStats account number: " + acctNo
                            + " last sent out alerts count " + msgCount);
        }
    }


}
