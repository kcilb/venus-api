package com.neptunesoftware.venusApis.Services;

import com.neptunesoftware.venusApis.Models.AlertCharge;
import com.neptunesoftware.venusApis.Models.ApiResponse;
import com.neptunesoftware.venusApis.Models.Response;
import com.neptunesoftware.venusApis.Repository.AdminDao;
import com.neptunesoftware.venusApis.Util.Logging;
import com.neptunesoftware.venusApis.Util.StaticRefs;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final AdminDao adminDao;

    public AdminService(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    public ApiResponse<List<Map<String, Object>>> findInstitutionCurrencies() {
        try {
            List<Map<String, Object>> list = adminDao.findInstitutionCurrencies();
            return ApiResponse.<List<Map<String, Object>>>builder().data(list)
                    .response(list.isEmpty() ? StaticRefs.noRecords() : StaticRefs.success())
                    .build();
        } catch (Exception e) {
            Logging.info(e.getMessage());
            return ApiResponse.<List<Map<String, Object>>>builder().data(null)
                    .response(StaticRefs.serverError()).build();
        }
    }

    public ApiResponse<List<Map<String, Object>>> findSMSAlertCurrencies(Integer alertCrncyId) {
        try {
            List<Map<String, Object>> list = adminDao.findSMSAlertCurrencies(alertCrncyId);
            if (list.isEmpty()) {
                return ApiResponse.<List<Map<String, Object>>>builder().data(null)
                        .response(StaticRefs.noRecords()).build();
            } else {
                return ApiResponse.<List<Map<String, Object>>>builder().data(list).build();
            }
        } catch (Exception e) {
            Logging.info(e.getMessage());
            return ApiResponse.<List<Map<String, Object>>>builder().data(null)
                    .response(StaticRefs.serverError()).build();
        }
    }

    public Response maintainSMSAlertCurrency(Map<String, Object> map) {
        try {
            if (map.get("sms_alert_crncy_id") == null) {
                adminDao.createSMSAlertCurrency(map);
            } else
                adminDao.updateSMSAlertCurrency(map);

            return StaticRefs.success();
        } catch (Exception e) {
            Logging.info(e.getMessage());
            return StaticRefs.serverError();
        }
    }

    public ApiResponse<List<AlertCharge>> findCharges(Integer alertCrncyId) {
        try {
            List<AlertCharge> list = adminDao.findCharges(alertCrncyId);
            if (list.isEmpty()) {
                return ApiResponse.<List<AlertCharge>>builder().data(null)
                        .response(StaticRefs.noRecords()).build();
            } else {
                return ApiResponse.<List<AlertCharge>>builder().data(list).build();
            }
        } catch (Exception e) {
            Logging.info(e.getMessage());
            return ApiResponse.<List<AlertCharge>>builder().data(null)
                    .response(StaticRefs.serverError()).build();
        }
    }

    public Response maintainCharge(Map<String, Object> map) {
        try {
            if (map.get("ptid") == null) {
                adminDao.createCharge(map);
            } else
                adminDao.removeCharge(map);

            return StaticRefs.success();
        } catch (Exception e) {
            Logging.info(e.getMessage());
            return StaticRefs.serverError();
        }
    }
}
