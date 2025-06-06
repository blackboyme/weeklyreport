package com.caac.weeklyreport;

import com.caac.weeklyreport.common.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 返回结果类
 * @author liaopeng
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResultBean<T> {
	private int code;
	private String message;
	private T data;
	private Object dataMessage; // 动态传入数据
	

	public ResultBean(ResultCode resultCode){
		this.code = resultCode.getCode();
		this.message = resultCode.getMeassage();
	}

	public ResultBean(ResultCode resultCode, T data){
		this.code = resultCode.getCode();
		this.message = resultCode.getMeassage();
		this.data = data;
	}

	public ResultBean(String msg, T data){
		this.message = msg;
		this.data = data;
	}

	public ResultBean(String msg){
		this.message = msg;
	}

	public ResultBean(ResultCode resultCode, String msg){
		this.code = resultCode.getCode();
		this.message = msg;
	}

	public ResultBean(ResultCode resultCode, String msg,T data){
		this.code = resultCode.getCode();
		this.message = msg;
		this.data = data;
	}

	public ResultBean(ResultCode resultCode, T data, Object datamsg){
		this.code = resultCode.getCode();
		this.message = resultCode.getMeassage();
		this.data= data;
		this.dataMessage = datamsg;
	}

	/**
	 * 错误处理-加上动态返回数据
	 * @param code
	 * @param message
	 * @return
	 */
	public static ResultBean error(Integer code,String message,String dataMessage) {
		ResultBean resultBean = new ResultBean();
		resultBean.setCode(code);
		resultBean.setMessage(message);
		resultBean.setDataMessage(dataMessage);
		return resultBean;
	}

	/**
	 * 默认成功方法
	 * @param
	 * @author liaopeng
	 */
	public static ResultBean success(){
		return new ResultBean(ResultCode.SUCCESS);
	}

	/**
	 * 成功，带数据
	 * @param data
	 * @author liaopeng
	 */
	public static <T> ResultBean success(T data){
		return success().setData(data);
	}

	/**
	 * 成功，带数据
	 * @param data
	 * @author liaopeng
	 */
	public static <T> ResultBean success(String msg,T data){
		return new ResultBean(ResultCode.SUCCESS,msg,data);
	}

	/**
	 * 成功，带msg
	 * @param msg
	 * @author liaopeng
	 */
	public static ResultBean success(String msg){
		return new ResultBean(ResultCode.SUCCESS,msg);
	}

	/**
	 * 成功，带dataMessage数据
	 * @param data
	 * @author liaopeng
	 */
	public static <T> ResultBean success(ResultCode resultCode, T data, Object dataMessage){

		return new ResultBean(resultCode,data,dataMessage);
	}

	/**
	 * 成功，带数据
	 * @param data
	 * @author liaopeng
	 */
	public static <T> ResultBean success(ResultCode resultCode,String msg,T data){
		return new ResultBean(resultCode,msg,data);
	}


	/**
	 * 成功，带返回码
	 * @param
	 * @author liaopeng
	 */
	public static ResultBean success(ResultCode resultCode){
		return new ResultBean(resultCode);
	}

	/**
	 * 默认失败方法
	 * @param
	 * @author liaopeng
	 */
	public static ResultBean fail(){
		return new ResultBean(ResultCode.FAILED);
	}


	/**
	 * 失败，带msg
	 * @param msg
	 * @author liaopeng
	 */
	public static ResultBean fail(String msg){
		return new ResultBean(ResultCode.FAILED,msg);
	}


	/**
	 * 失败，带数据
	 * @param
	 * @author liaopeng
	 */
	public static <T> ResultBean fail(T data){
		return fail().setData(data);
	}


	/**
	 * 失败，带数据
	 * @param data
	 * @author liaopeng
	 */
	public static <T> ResultBean fail(ResultCode resultCode,String msg,T data){
		return new ResultBean(resultCode,msg,data);
	}


	/**
	 * 失败，带数据
	 * @param data
	 * @author liaopeng
	 */
	public static <T> ResultBean fail(String msg,T data){
		return new ResultBean(ResultCode.FAILED,msg,data);
	}

	/**
	 * 失败，带错误码
	 * @param
	 * @author liaopeng
	 */
	public static <T> ResultBean fail(ResultCode resultCode){
		return new ResultBean(resultCode);
	}

	/**
	 * 失败，带错误码
	 * @param
	 * @author liaopeng
	 */
	public static <T> ResultBean fail(ResultCode resultCode, T data){
		return new ResultBean(resultCode, data);
	}

	/**
	 * 失败，带错误信息
	 * @param
	 * @author liaopeng
	 */
	public static <T> ResultBean fail(ResultCode resultCode, String msg){
		return new ResultBean(resultCode, msg);
	}
	
}
