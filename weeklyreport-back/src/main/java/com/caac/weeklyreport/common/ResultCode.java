package com.caac.weeklyreport.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 业务操纵返回值辅助类
 *
 * @author hanrenjie
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResultCode {

    /**
     * 公共代码
     */
    FAILED(-1, "操作失败"),
    ERROR(-1, "系统内部异常"),
    SUCCESS(0, "操作成功"),
    PARAM_IS_NULL(2, "传入参数有空"),
    PARAM_IS_ERROR(3, "参数错误"),
    PARAM_NOT_NORMAL(4,"传入的参数错误或者格式不正确,请检查参数"),
    DATA_ALREADY_EXIST(5, "数据已经存在"),
    NOT_ALLOW_DELETE(6, "不允许删除"),
    DICTNAME_IS_NULL(7, "数据字典名为空"),
    ERROR_HTTP_REQUEST(8, "网络异常，请刷新或重新登录"),
    URL_NOT_FOUND(9,"请求路径不正确"),
    /**
     * 业务异常1001-2000 (hanrenjie)
     */
    // 登录中的token处理
    DECODE_IS_NULL(1001,"解析参数失败, 请检查！"),
    JWT_FAIL(1002,"生成token失败！"),
    USER_FAIL(1003,"用户查询失败，不存在该用户!"),
    ACCESS_ILLEGAL(1004,"非法操作！"),
    TOKEN_MAKE_FAIL(1005,"生成Token失败！"),

    // 拦截器中的token处理
    TOKEN_IS_NULL(1006,"无token,请先登录！"),
    LOGIN_ILLEGAL(1007,"非法访问！请联系管理员添加账号！"),
    LOGIN_UNUSAL(1008,"token异常!"),
    NO_AUTH(1009,"用户未认证，请重新登录"),
    TOKEN_INVAALID(1010,"token验证失败！"),
    TOKEN_OVERTIME(1011,"token已过期!"),
    LOGIN_ANOTHER_EQUIPMENT(1012,"您已在其他设备上登录，请重新登录"),
    //退出登录
    LOG_OUT(1013,"已退出登录"),

    // 用户菜单角色
    USER_MENU_ERROR(2001,"用户菜单获取失败"),
    USER_ROLE_ERROR(2002,"用户角色获取失败"),
    MENU_ADD_ERROR(2003,"新增菜单失败"),
    MENU_DEL_ERROR(2004,"删除菜单失败"),
    MENU_UP_ERROR(2005,"更新菜单失败"),
    USER_ROLE_REPEAT_ERROR(2006,"该角色已存在，勿重复添加"),
    USER_ADD_ERROR(2007,"用户全部添加失败"),

    //文件
    FILE_UPLOAD_SUCCESS(3001,"文件上传成功"),
    FILE_UPLOAD_FAILED(3002,"文件上传失败"),
    FILE_DOWNLOAD_SUCCESS(3003,"文件下载成功"),
    FILE_DOWNLOAD_FAILED(3004,"文件下载失败"),
    FILE_EMPTY(3005,"文件名为空");


    private int code;
    private String meassage;

}
