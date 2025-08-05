package com.neptunesoftware.venusApis.Models;

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
    
}
