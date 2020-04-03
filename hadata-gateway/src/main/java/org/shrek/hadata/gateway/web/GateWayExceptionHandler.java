package org.shrek.hadata.gateway.web;

import org.shrek.hadata.commons.exception.HadataException;
import org.shrek.hadata.commons.web.BasicResult;
import org.shrek.hadata.commons.web.BasicResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * GateWay 统一异常处理
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年05月07日 09:13
 */
@ControllerAdvice
public class GateWayExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value = HadataException.class)
    public BasicResult<String> defaultErrorHandler(HttpServletRequest req, HadataException e) throws Exception {
        return BasicResult.fail(BasicResultCode.HTTP_ERROR, e.getMessage());
    }
}
