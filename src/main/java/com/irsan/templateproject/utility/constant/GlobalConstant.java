package com.irsan.templateproject.utility.constant;

import java.util.concurrent.TimeUnit;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 17/05/2024
 */
public class GlobalConstant {

    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
    public static final String KEYSTORE_PATH = "classpath:keystore/";
    public static final String ERROR_STACKTRACE_LOG_PATH = "classpath:keystore/";
    public static final long EXPIRE_DURATION = TimeUnit.HOURS.toMillis(1); //1 hour

}
