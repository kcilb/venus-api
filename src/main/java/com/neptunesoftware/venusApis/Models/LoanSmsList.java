
package com.neptunesoftware.venusApis.Models;

import java.util.List;

public class LoanSmsList
{
    public String responseCode;
    public String responseMsg;
    public List<Message> entry;
    
    public LoanSmsList(final String responseCode, final String responseMsg, final List<Message> smsList) {
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
        this.entry = smsList;
    }
    
    public LoanSmsList() {
    }
    
    public static class Message
    {
        public int recordID;
        public String phoneNo;
        public String email;
        public String accountNo;
        public String tranNaration;
        public String sender;
        public String chargeable;
        
        public Message(final int recordID, final String phoneNo, final String email, final String accountNo, final String tranNaration, final String sender, final String chargeable) {
            this.recordID = recordID;
            this.phoneNo = phoneNo;
            this.email = email;
            this.accountNo = accountNo;
            this.tranNaration = tranNaration;
            this.sender = sender;
            this.chargeable = chargeable;
        }
        
        public Message() {
        }
    }
}
