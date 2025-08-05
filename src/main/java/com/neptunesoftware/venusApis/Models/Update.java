/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptunesoftware.venusApis.Models;

/**
 * @author Olupot.D
 *
 */

public class Update {

	private String errorCode, errorDesc, msgId;

	public Update() {
		super();
	}

	public Update(String errorCode, String errorDesc, String msgId) {
		super();
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
		this.msgId = msgId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

}
