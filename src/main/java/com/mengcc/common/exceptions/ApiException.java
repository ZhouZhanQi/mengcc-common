package com.mengcc.common.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * 接口错误代码
 *
 * @author zhouzq
 * @date 2017/2/4
 */
@Slf4j
public class ApiException extends BaseUnCheckedException {

    public ApiException(int errCode) {
        super(errCode);
    }

    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(String msg, int errCode) {
        super(msg, errCode);
    }

    public ApiException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
