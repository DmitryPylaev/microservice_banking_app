package com.neoflex.java2023.util;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomLogger {
    public static void logInfoClassAndMethod() {
        log.info("В классе " + new Exception().getStackTrace()[1].getClassName() + " вызван метод: " + new Exception().getStackTrace()[1].getMethodName());
    }

    public static void logInfoRequest(Object request) {
        log.info("Запрос: " + request);
    }
}
