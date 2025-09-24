package com.neptunesoftware.venusApis.Services;

import com.neptunesoftware.supernova.ws.common.XAPIException;
import com.neptunesoftware.supernova.ws.common.XAPIRequestBaseObject;
import com.neptunesoftware.supernova.ws.server.account.data.AccountBalanceOutputData;
import com.neptunesoftware.supernova.ws.server.account.data.AccountBalanceRequest;
import com.neptunesoftware.supernova.ws.server.transaction.data.GLTransferOutputData;
import com.neptunesoftware.supernova.ws.server.transaction.data.GLTransferRequest;
import com.neptunesoftware.supernova.ws.server.transaction.data.TxnResponseOutputData;
import com.neptunesoftware.supernova.ws.server.txnprocess.data.XAPIBaseTxnRequestData;
import com.neptunesoftware.venusApis.Beans.AppProps;
import com.neptunesoftware.venusApis.Beans.ItemCacheService;
import com.neptunesoftware.venusApis.DTOs.ChargeProcessDTO;
import com.neptunesoftware.venusApis.DTOs.ChargeTierDTO;
import com.neptunesoftware.venusApis.Models.AlertCharge;
import com.neptunesoftware.venusApis.Models.AlertRequest;
import com.neptunesoftware.venusApis.Models.ApiResponse;
import com.neptunesoftware.venusApis.Models.SMSChargeLog;
import com.neptunesoftware.venusApis.Repository.AlertsDao;
import com.neptunesoftware.venusApis.Repository.ChargeDao;
import com.neptunesoftware.venusApis.Util.Logging;
import com.neptunesoftware.venusApis.Util.StaticRefs;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ChargeService {

    private final ChargeDao chargeDao;

    public ChargeService(ChargeDao chargeDao) {
        this.chargeDao = chargeDao;
    }

    public ApiResponse<?> findChargeHistory(ChargeProcessDTO request) {
        try {
            List<SMSChargeLog> list = chargeDao.findChargeHistory(request.startDate, request.endDate);
            if (list.isEmpty())
                return ApiResponse.builder().data(null)
                        .response(StaticRefs.noRecords()).build();

            return ApiResponse.builder().data(list)
                    .response(StaticRefs.success())
                    .build();

        } catch (Exception e) {
            Logging.error(e.getMessage(), e);
            return ApiResponse.builder().data(null)
                    .response(StaticRefs.serverError()).build();
        }
    }


}
