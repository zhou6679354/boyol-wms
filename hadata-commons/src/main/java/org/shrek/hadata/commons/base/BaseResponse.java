package org.shrek.hadata.commons.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 内部项目调用结果
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年08月27日 09:02
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean flag;
    private String message;
    private String subCode;
    private String subMessage;
    private T data;



    public static <T> BaseResponse<T> success() {
        return success(BaseResultCode.SUCCESS.getCode(), BaseResultCode.SUCCESS.getMessage(), null);
    }

    public static <T> BaseResponse<T> success(T data) {
        return success(BaseResultCode.SUCCESS.getCode(), BaseResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> BaseResponse<T> success(String subCode, String subMessage, T data) {
        BaseResponse<T> baseResult = new BaseResponse<>();
        baseResult.setFlag(true);
        baseResult.setMessage("调用成功!");
        baseResult.setSubCode(subCode);
        baseResult.setSubMessage(subMessage);
        baseResult.setData(data);
        return baseResult;
    }

    public static <T> BaseResponse<T> fail() {
        return fail(BaseResultCode.FAILURE.getCode(), BaseResultCode.FAILURE.getMessage());
    }

    public static <T> BaseResponse<T> fail(String subMessage) {
        return fail(BaseResultCode.FAILURE.getCode(), subMessage);
    }
    public static <T> BaseResponse<T> fail(BaseResultCode baseResultCode) {
        return fail(baseResultCode.getCode(),baseResultCode.getMessage());
    }
    public static <T> BaseResponse<T> fail(String subCode, String subMessage) {
        BaseResponse<T> baseResult = new BaseResponse<>();
        baseResult.setFlag(false);
        baseResult.setMessage("调用失败!");
        baseResult.setSubCode(subCode);
        baseResult.setSubMessage(subMessage);
        return baseResult;
    }

    public static enum BaseResultCode {
        SUCCESS("200", "执行成功!"),
        FAILURE("400", "执行失败!");

        @Getter
        @Setter
        private String code;

        @Getter
        @Setter
        private String message;

        BaseResultCode(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }

}
