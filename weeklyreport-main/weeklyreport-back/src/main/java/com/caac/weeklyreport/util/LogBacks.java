package com.caac.weeklyreport.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassNames:LogBacks
 * @Description:
 * @Author:
 * @Date: 2020/6/12 16:36
 * @Version: V1.0
 **/
public class LogBacks {
    public  static final Logger LOGGER = LoggerFactory.getLogger(getClassName());

    /**
     *@Description //打印错误
     *@Param: [msg]
     *@Return: void
     *@Author: cc
     *@Date: 2020/6/12
     **/
    public static void error(String msg) {

        LoggerFactory.getLogger(getClassName()).error(msg);

    }
    /**
     *@Description //打印错误
     *@Param: [msg, obj]
     *@Return: void
     *@Author: cc
     *@Date: 2020/6/12
     **/
    public static void error(String msg, Object... obj) {

        LoggerFactory.getLogger(getClassName()).error(msg, obj);

    }
    /**
     *@Description //打印warn级信息
     *@Param: [msg]
     *@Return: void
     *@Author: cc
     *@Date: 2020/6/12
     **/
    public static void warn(String msg) {

        LoggerFactory.getLogger(getClassName()).warn(msg);

    }
    /**
     *@Description //打印warn级信息
     *@Param: [msg, obj]
     *@Return: void
     *@Author: cc
     *@Date: 2020/6/12
     **/
    public static void warn(String msg, Object... obj) {

        LoggerFactory.getLogger(getClassName()).warn(msg, obj);

    }
    /**
     *@Description //打印info级信息
     *@Param: [msg]
     *@Return: void
     *@Author: cc
     *@Date: 2020/6/12
     **/
    public static void info(String msg) {

        LoggerFactory.getLogger(getClassName()).info(msg);

    }
    /**
     *@Description //打印info级信息
     *@Param: [msg, obj]
     *@Return: void
     *@Author: cc
     *@Date: 2020/6/12
     **/
    public static void info(String msg, Object... obj) {

        LoggerFactory.getLogger(getClassName()).info(msg, obj);

    }
    /**
     *@Description //打印debug级信息
     *@Param: [msg]
     *@Return: void
     *@Author: cc
     *@Date: 2020/6/12
     **/
    public static void debug(String msg) {
        LoggerFactory.getLogger(getClassName()).debug(msg);
    }
    /**
     *@Description //打印debug级信息
     *@Param: [msg, obj]
     *@Return: void
     *@Author: cc
     *@Date: 2020/6/12
     **/
    public static void debug(String msg, Object... obj) {
        LoggerFactory.getLogger(getClassName()).debug(msg, obj);
    }
    /**
     *@Description //获取调用 error,info,debug静态类的类名
     *@Param: []
     *@Return: java.lang.String
     *@Author: cc
     *@Date: 2020/6/12
     **/
    private static String getClassName() {
        return new SecurityManager() {
            public String getClassName() {
                return getClassContext()[3].getName();
            }
        }.getClassName();

    }

}
