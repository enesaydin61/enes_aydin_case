package com.insider.cookie;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface Cookies {

  String key() default "";

  String value() default "";

  String domain() default "useinsider.com";

  String path() default "/";

  int expiry() default 0;

}
