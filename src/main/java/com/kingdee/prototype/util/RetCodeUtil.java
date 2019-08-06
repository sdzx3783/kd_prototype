package com.kingdee.prototype.util;


import com.kingdee.prototype.base.enums.RetCode;

public class RetCodeUtil {

	public static boolean isSucc(String retCode){
		return retCode.equals(RetCode.SUCCESS.retCode);
	}
}
