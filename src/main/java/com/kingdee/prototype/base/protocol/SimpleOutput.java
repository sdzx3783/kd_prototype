package com.kingdee.prototype.base.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleOutput<T> {
	
	
	private String retCode;
	
	private String msg;
	
	private T retData;

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getRetData() {
		return retData;
	}

	public void setRetData(T retData) {
		this.retData = retData;
	}
	
	
}
