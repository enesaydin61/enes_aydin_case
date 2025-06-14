package com.insider.provider;

import com.insider.context.WebTestContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebTestContextProvider {

    private static final ThreadLocal<WebTestContext> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(WebTestContext context) {
        THREAD_LOCAL.set(context);
    }

    public static WebTestContext get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        try {
            THREAD_LOCAL.remove();
        } catch (Exception exception) {
            log.warn("Could Not Remove Thread_Local.");
            throw exception;
        }
    }
} 