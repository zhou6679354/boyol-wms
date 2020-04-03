package org.shrek.hadata.commons.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月07日 07:26
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "response")
public class BasicResult<T> implements Serializable {

    /**
     * 自定义的状态码,框架会把code的前面三位作为http的status设置到response, 后面4位为不同的异常进行定义
     * 对于业务逻辑异常，建议code设置为2001001, 200表示请求正常，1001表示业务逻辑异常，比如支付没有足够金额
     */
    @JsonView(BasicResultView.class)
    private int code;
    //
    /**
     * 返回的用户展示的详细信息
     */
    @JsonView(BasicResultView.class)
    private String msg;
    /**
     * 用于内部具体定位错误的信息
     */
    @JsonView(BasicResultView.class)
    private String detailMsg;
    /**
     * 返回的具体类型
     */
    @JsonView(BasicResultView.class)
    private T data;

    public static <T> BasicResult<T> fail(BasicResultCode basicResultCode, String detailMsg) {
        return fail(basicResultCode.getCode(), basicResultCode.getMsg(), detailMsg);
    }

    public static <T> BasicResult<T> fail(int code, String msg) {
        return fail(code, msg, msg);
    }

    public static <T> BasicResult<T> fail(int code, String msg, String detailMsg) {
        BasicResult<T> r = new BasicResult<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setDetailMsg(detailMsg);
        return r;
    }

    public static <T> BasicResult<T> success() {
        return success(null, BasicResultCode.SUCCESS.getMsg(), BasicResultCode.SUCCESS.getMsg());
    }

    public static <T> BasicResult<T> success(T t) {
        return success(t, BasicResultCode.SUCCESS.getMsg(), BasicResultCode.SUCCESS.getMsg());
    }

    public static <T> BasicResult<T> success(T t, String msg) {
        return success(t, msg, msg);
    }

    public static <T> BasicResult<T> success(T t, String msg, String detailMsg) {
        BasicResult<T> r = new BasicResult<>();
        r.setCode(BasicResultCode.SUCCESS.getCode());
        r.setMsg(msg);
        r.setDetailMsg(detailMsg);
        r.setData(t);
        return r;
    }

    interface BasicResultView {

    }
}