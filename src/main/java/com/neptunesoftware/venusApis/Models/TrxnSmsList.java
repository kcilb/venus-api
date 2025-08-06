package com.neptunesoftware.venusApis.Models;

import java.util.ArrayList;
import java.util.List;

public class TrxnSmsList
{
    public String responseCode;
    public String responseMsg;
    public List<SMS> smsList;
    
    public TrxnSmsList(final String responseCode, final String responseMsg, final List<SMS> smsList) {
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
        this.smsList = smsList;
    }
    
    public TrxnSmsList() {
    }

    public TrxnSmsList createDummyResponse() {
        TrxnSmsList dummyResponse = new TrxnSmsList();

        // Set response code and message
        dummyResponse.responseCode = "00";
        dummyResponse.responseMsg = "Success";

        // Create dummy SMS list

        List<SMS> smsList = new ArrayList<SMS>();
        SMS txn1 = new SMS();
        txn1.setRecordID(1001);
        txn1.setCustomerId(5001);
        txn1.setCustName("John Doe");
        txn1.setPhoneNo("2348012345678");
        txn1.setAccountNo("1234567890");
        txn1.setTranType("Credit");
        txn1.setTranAmount("5,000.00");
        txn1.setTranCurrency("NGN");
        txn1.setTranNaration("Salary payment");
        txn1.setTranTime("2023-05-15 09:30:45");
        txn1.setTranChannel("Mobile App");
        txn1.setAcctBalAfterTran("75,230.50");
        smsList.add(txn1);

        txn1 = new SMS();
        txn1.setRecordID(101);
        txn1.setCustomerId(5001);
        txn1.setCustName("John Doe");
        txn1.setPhoneNo("2348012345678");
        txn1.setAccountNo("1234567890");
        txn1.setTranType("Credit");
        txn1.setTranAmount("5,000.00");
        txn1.setTranCurrency("NGN");
        txn1.setTranNaration("Salary payment");
        txn1.setTranTime("2023-05-15 09:30:45");
        txn1.setTranChannel("Mobile App");
        txn1.setAcctBalAfterTran("75,230.50");
        smsList.add(txn1);
        dummyResponse.smsList = smsList;

        return dummyResponse;
    }
    
}
