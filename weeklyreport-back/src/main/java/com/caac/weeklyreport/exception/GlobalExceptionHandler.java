package com.caac.weeklyreport.exception;


import com.caac.weeklyreport.common.ResultBean;
import com.caac.weeklyreport.util.LogBacks;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author hanrenjie on 2020/5/13 14:41
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final int BADPARAMCODE = 400;
    private static final int SYSTEMTERROR = 444;
    private static final int NOHANDLER = 404;

    /**
     * @author：
     * @param: ex
     * @description: 一般业务异常
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResultBean<?> handleBusinessException(BusinessException ex){
        LogBacks.error(ex.getDesc(), ex);
        return new ResultBean<>(ex.getCode(),ex.getDesc(),ex.getMessage(),ex.getDescCode());
    }

    /**
     * @author：hanrenjie on 2020/5/13 16:12
     * @param: ex
     * @description: post参数异常处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultBean<?> handleException(MethodArgumentNotValidException ex) {
        LogBacks.error( "请求参数错误!!", ex);
        return new ResultBean<>(BADPARAMCODE,"请求参数错误!!", Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage(),null);
    }

    /**
     * @author：hanrenjie on 2020/5/13 16:13
     * @param: ex
     * @description: get参数异常处理
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public ResultBean<?> handleException(MissingServletRequestParameterException ex) {
        LogBacks.error("请求参数异常!!", ex);
        return new ResultBean<>(BADPARAMCODE,"请求参数异常!!",ex.getMessage(),null);
    }

    /**
     * @author：hanrenjie on 2020/5/13 16:13
     * @param: ex
     * @description: get参数异常处理
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultBean<?> handleException(Exception ex) {
        LogBacks.error("操作失败!!", ex);
        return new ResultBean<>(SYSTEMTERROR,"操作失败!!",ex.getMessage(),null);
    }

    /**
     * @author：hanrenjie on 2020/6/23 18:31
     * @param: ex
     * @description: 路径不存在404异常
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseBody
    public ResultBean<?> handleException(NoHandlerFoundException ex) {
        LogBacks.error( "当前路径不存在!!", ex);
        return new ResultBean<>(NOHANDLER,"当前路径不存在!!",ex.getMessage(),null);
    }

    /**
     * 空指针异常
     * @param ex
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultBean<?> handleException(NullPointerException ex) {
        LogBacks.error("null指针异常!!", ex);
        return new ResultBean<>(SYSTEMTERROR,"null指针异常!!",ex.getMessage(),null);
    }

    /**
     * 非法数组
     * @param ex
     * @return
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ResultBean<?> handleException(IllegalArgumentException ex) {
        LogBacks.error( "非法数组!!", ex);
        return new ResultBean<>(SYSTEMTERROR,"非法数组!!",ex.getMessage(),null);
    }

    /**
     * SQL异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(value = SQLException.class)
    @ResponseBody
    public ResultBean<?> handleException(SQLException ex) {
        LogBacks.error( "数据库操作异常", ex);
        return new ResultBean<>(SYSTEMTERROR,"数据库操作异常",ex.getMessage(),null);
    }

    /**
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = IOException.class)
    @ResponseBody
    public ResultBean<?> handleException(IOException ex) {
        LogBacks.error("I/O操作异常", ex);
        return new ResultBean<>(SYSTEMTERROR,"I/O操作异常",ex.getMessage(),null);
    }
}
