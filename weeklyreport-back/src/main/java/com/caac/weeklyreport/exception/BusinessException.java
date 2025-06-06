package com.caac.weeklyreport.exception;



import com.caac.weeklyreport.common.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 业务异常
 * @author zhangliang
 *
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 6878340599741511945L;
	/**
	 * 异常代码
	 */
	private int code;
	/**
	 * 异常动态信息
	 */
	private String descCode;
	/**
	 * 异常描述信息
	 */
	private String desc;
	
	/**
	 * 描述自定异常码和异常信息
	 * @author yixin
	 * @param resultCode
	 * @param descCode
	 */
	public BusinessException(ResultCode resultCode, String descCode){
		this.code = resultCode.getCode();
		this.desc = resultCode.getMeassage();
		this.descCode = descCode;
	}


	/**
	 * 自定义异常码和异常信息
	 * @author Gzj
	 * @param code
	 * @param msg
	 */
	public BusinessException(int code, String msg){
		this.code = code;
		this.desc = msg;
	}


	/**
	 * 自定一个异常时使用
	 * @param resultCode
	 * @author liaopeng
	 */
	public BusinessException(ResultCode resultCode){
		super(resultCode.getMeassage());
		this.code = resultCode.getCode();
		this.desc = resultCode.getMeassage();
	}

	/**
	 * 捕获其他异常时使用
	 * @param resultCode
	 * @param cause
	 * @author liaopeng
	 */
	public BusinessException(ResultCode resultCode, Throwable cause){
		super(resultCode.getMeassage(),cause,true,true);
		this.code = resultCode.getCode();
		this.desc = resultCode.getMeassage();
	}

	/**
	 * 自定错误消息
	 * @author liaopeng
	 */
	public BusinessException(String message){
		super(message);
		this.code = ResultCode.ERROR.getCode();
		this.desc = message;
	}

	public BusinessException(String message, Throwable cause) {
		super(message,cause);
	}
	public BusinessException(Throwable cause) {
		super(cause);
	}
	public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message,cause,enableSuppression,writableStackTrace);
	}
	public BusinessException(Throwable cause, String desc) {
		super(cause);
		this.desc=desc;
	}


	
}
