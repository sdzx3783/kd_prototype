package com.kingdee.prototype.base;

import com.kingdee.prototype.base.enums.RetCode;
import com.kingdee.prototype.base.protocol.SimpleOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理
 * 
 * @author admin
 *
 */

@ControllerAdvice
public class GlobalExceptionHandler {

	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = { Exception.class })
	@ResponseBody
	public SimpleOutput exceptionHandler(Exception e, HttpServletResponse response) {
		logger.error("服务器发送不可预期的错误,msg:{}", e.getMessage());
		return new SimpleOutput(RetCode.FAIL.retCode, RetCode.FAIL.message,null);
	}
}
