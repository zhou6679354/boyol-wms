package org.shrek.hadata.commons.web;

import lombok.Getter;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月07日 07:31
 */
@Getter
public enum BasicResultCode {
    SUCCESS(200, "success"),


    THROWABLE_ERROR(500, "throwable error"),
    RUNTIME_ERROR(500, "运行时异常"),
    SQL_ERROR(500, "SQL 异常"),
    REDIS_ERROR(500, "Redis 异常"),
    METHOD_ARGUMENT_MISS_ERROR(400, "方法参数错误"),
    METHOD_ARGUMENT_VALIDATE_ERROR(400, "方法参数验证错误"),
    METHOD_NOT_ALLOW_ERROR(405, "method not allow error"),
    UNSUPPORTED_MEDIA_TYPE_ERROR(415, "不支持的媒体类型错误"),
    SECURITY_ACCESS_DENIED(401, "未授权访问!"),
    SECURITY_AUTHENTICATION(401, "身份验证失败,请输入正确信息!"),
    SECURITY_TOKEN_NOT_ACTIVE(401, "TOKEN 已失效，刷新token或者重新获取!"),
    HTTP_CONNECTION_TIME_OUT(402, "http请求连接超时"),
    HTTP_HYSTRIX_TIME_OUT(402, "http读超时"),
    HTTP_READ_TIME_OUT(402, "http读超时"),
    HTTP_SERVICE_NOT_AVAILABLE(402, "内部系统调用服务不可用"),
    HTTP_ERROR(402, "http内部调用服务错误");

    private int code;
    private String msg;
    private String detailMsg;

    BasicResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.detailMsg = msg;
    }

    BasicResultCode(int code, String msg, String detailMsg) {
        this.code = code;
        this.msg = msg;
        this.detailMsg = detailMsg;
    }
}
